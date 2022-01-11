package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            LOG.debug("verify OpArith: start");
            Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//            Validate.notNull(localEnv, "Env_exp object should not be null");

            Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            Type returnType;

            if ((typeLeftOp.isInt() && typeRightOp.isInt()) || (typeLeftOp.isFloat() && typeRightOp.isInt())) {
                // Cases : (int, int) and (float, int)
                this.setType(typeLeftOp);
                returnType = typeLeftOp;
                if (typeLeftOp.isFloat()) {
                    // Implicit float conversion
                    this.setRightOperand(new ConvFloat(this.getRightOperand()));
                }
            } else if (typeRightOp.isFloat()) {
                // Cases : (int, float) and (float, float)
                this.setType(typeRightOp);
                returnType = typeRightOp;
                if (typeLeftOp.isInt()) {
                    // Implicit float conversion
                    this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                }
            } else {
                throw new ContextualError("Binary operation is only allowed for int or float types", this.getLocation());
            }

            LOG.debug("verify OpArith: end");
            return returnType;
    }

    /**
     * Add an instruction corresponding to the arithmetical operator, between dval and gpRegister
     *
     * @param compiler
     * @param dval
     * @param gpRegister
     */
    protected void mnemo(DecacCompiler compiler, DVal dval, GPRegister gpRegister) {
        switch (this.getOperatorName()) {
            case "+":
                compiler.addInstruction(new ADD(dval, gpRegister));
                break;
            case "-":
                compiler.addInstruction(new SUB(dval, gpRegister));
                break;
            case "*":
                compiler.addInstruction(new MUL(dval, gpRegister));
                break;
            case "/":
                compiler.addInstruction(new DIV(dval, gpRegister));
                break;
            case "%":
                // TODO: modulo instruction
                throw new UnsupportedOperationException("% not yet implemented");
        }
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        DVal rightDval = this.getRightOperand().dval(compiler);
        if (rightDval != null) {
            this.getLeftOperand().codeGenExpr(compiler, n);
            this.mnemo(compiler, rightDval, Register.getR(n));
        } else {
            int maxRegister = compiler.getCompilerOptions().getRegisterNumber();
            if (n < maxRegister) {
                this.getLeftOperand().codeGenExpr(compiler, n);
                this.getRightOperand().codeGenExpr(compiler, n+1);
                this.mnemo(compiler, Register.getR(n+1), Register.getR(n));
            } else {
                this.getLeftOperand().codeGenExpr(compiler, n);
                compiler.addInstruction(new PUSH(Register.getR(n)), "; sauvegarde");
                this.getRightOperand().codeGenExpr(compiler, n);
                compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
                compiler.addInstruction(new POP(Register.getR(n)), "; restauration");
                this.mnemo(compiler, Register.R0, Register.getR(n));
            }
        }
    }
}
