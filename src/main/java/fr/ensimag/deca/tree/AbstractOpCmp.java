package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify OPComp: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type booleanType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("boolean")).getType();

        boolean isEqOrNeq = this.getOperatorName().equals("!=") || this.getOperatorName().equals("==");
        boolean isIntOrFloat = (typeLeftOp.isFloat() || typeLeftOp.isInt()) && (typeRightOp.isFloat() || typeRightOp.isInt());
        boolean isClassOrNull = (typeLeftOp.isClass() || typeLeftOp.isNull()) && (typeRightOp.isClass() || typeRightOp.isNull());
        boolean isBoolean = typeLeftOp.isBoolean() && typeRightOp.isBoolean();

        if (isEqOrNeq && !(isIntOrFloat || isClassOrNull || isBoolean)) {
            // For "==" and "!=" comparisons, the operand types must be int, float, class, null or boolean
            throw new ContextualError(
                    "Equals or not equals comparison is only with int, float, class, null or boolean types",
                    this.getLocation()
            );
        } else if (!isEqOrNeq && !isIntOrFloat) {
            // For other comparisons, only int or float types are allowed
            throw new ContextualError(
                    "This comparison works only with int or float types",
                    this.getLocation()
            );
        }

        // Implicit float conversion
        if (typeLeftOp.isInt() && typeRightOp.isFloat()) {
            this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
            this.getLeftOperand().setType(typeRightOp);
        } else if (typeLeftOp.isFloat() && typeRightOp.isInt()) {
            this.setRightOperand(new ConvFloat(this.getRightOperand()));
            this.getRightOperand().setType(typeLeftOp);
        }

        this.setType(booleanType);
        LOG.debug("verify OPComp: end");
        return booleanType;
    }

    /**
     * Add a branch instruction corresponding to the comparison operator
     *
     * @param compiler Deca Compiler used to add IMA instruction
     * @param bool Condition for goto branch
     * @param branch The branch where the program needs to go
     */
    protected void mnemo(DecacCompiler compiler, boolean bool, Label branch) {
        switch (this.getOperatorName()) {
            case "==":
                compiler.addInstruction(bool ? new BEQ(branch) : new BNE(branch));
                break;
            case "!=":
                compiler.addInstruction(bool ? new BNE(branch) : new BEQ(branch));
                break;
            case "<":
                compiler.addInstruction(bool ? new BLT(branch) : new BGE(branch));
                break;
            case "<=":
                compiler.addInstruction(bool ? new BLE(branch) : new BGT(branch));
                break;
            case ">":
                compiler.addInstruction(bool ? new BGT(branch) : new BLE(branch));
                break;
            case ">=":
                compiler.addInstruction(bool ? new BGE(branch) : new BLT(branch));
                break;
        }
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch) {
        DVal rightDval = this.getRightOperand().dval(compiler);

        // Evaluate left operand
        this.getLeftOperand().codeGenExpr(compiler, 2);

        if (rightDval != null) {
            compiler.addInstruction(new CMP(rightDval, Register.getR(2)));
        } else {
            this.getRightOperand().codeGenExpr(compiler, 3);
            compiler.addInstruction(new CMP(Register.getR(3), Register.getR(2)));
        }

        // Add branch instruction
        this.mnemo(compiler, bool, branch);
    }
}
