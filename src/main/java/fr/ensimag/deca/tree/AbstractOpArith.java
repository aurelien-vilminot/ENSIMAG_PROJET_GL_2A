package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
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
                    this.getRightOperand().setType(typeLeftOp);
                }
            } else if (typeRightOp.isFloat()) {
                // Cases : (int, float) and (float, float)
                this.setType(typeRightOp);
                returnType = typeRightOp;
                if (typeLeftOp.isInt()) {
                    // Implicit float conversion
                    this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                    this.getLeftOperand().setType(typeRightOp);
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
     * @param compiler Deca Compiler used to add IMA instruction
     * @param dval The left operand of the operation
     * @param gpRegister The right operand of the operation
     */
    protected void mnemo(DecacCompiler compiler, DVal dval, GPRegister gpRegister) {
        switch (this.getOperatorName()) {
            case "+":
                compiler.addInstruction(new ADD(dval, gpRegister));
                if (this.getType().isFloat()) {
                    compiler.addInstruction(new BOV(compiler.getLabelGenerator().getOverFlowLabel()));
                }
                break;
            case "-":
                compiler.addInstruction(new SUB(dval, gpRegister));
                if (this.getType().isFloat()) {
                    compiler.addInstruction(new BOV(compiler.getLabelGenerator().getOverFlowLabel()));
                }
                break;
            case "*":
                compiler.addInstruction(new MUL(dval, gpRegister));
                if (this.getType().isFloat()) {
                    compiler.addInstruction(new BOV(compiler.getLabelGenerator().getOverFlowLabel()));
                }
                break;
            case "/":
                if (this.getType().isInt()) {
                    compiler.addInstruction(new QUO(dval, gpRegister));

                } else if (this.getType().isFloat()) {
                    compiler.addInstruction(new DIV(dval, gpRegister));
                }
                compiler.addInstruction(new BOV(compiler.getLabelGenerator().getOverFlowLabel()));
                break;
            case "%":
                // Modulo operation with a loop
                Label modLabel = new Label(compiler.getLabelGenerator().generateLabel("mod"));
                compiler.addLabel(modLabel);
                compiler.addInstruction(new SUB(dval, gpRegister));
                compiler.addInstruction(new CMP(dval, gpRegister));
                compiler.addInstruction(new BGT(modLabel));
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
                compiler.addInstruction(new PUSH(Register.getR(n)), "sauvegarde");
                this.getRightOperand().codeGenExpr(compiler, n);
                compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
                compiler.addInstruction(new POP(Register.getR(n)), "restauration");
                this.mnemo(compiler, Register.R0, Register.getR(n));
            }
        }
    }
}
