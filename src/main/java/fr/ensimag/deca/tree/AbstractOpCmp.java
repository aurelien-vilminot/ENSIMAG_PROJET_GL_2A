package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
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
        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type booleanType = new BooleanType(compiler.getSymbolTable().create("boolean"));

        // For other comparisons than "==" and "!=", the operand types must be int or float
        if ((!this.getOperatorName().equals("!=") && !this.getOperatorName().equals("=="))
                && (!typeLeftOp.isFloat() || !typeLeftOp.isInt()) && (!typeRightOp.isFloat() || !typeRightOp.isInt())) {
            throw new ContextualError("Equals or not equals comparison is only with int or float types", this.getLocation());
        }

        // Implicit float conversion
        if (typeLeftOp.isInt() && typeRightOp.isFloat()) {
            this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
        } else if (typeLeftOp.isFloat() && typeRightOp.isInt()) {
            this.setRightOperand(new ConvFloat(this.getRightOperand()));
        }

        this.setType(booleanType);
        LOG.debug("verify OPComp: end");
        return booleanType;
    }
}
