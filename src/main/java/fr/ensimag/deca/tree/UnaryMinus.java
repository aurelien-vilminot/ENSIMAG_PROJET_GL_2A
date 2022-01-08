package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl07
 * @date 01/01/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify UnaryMinus: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);

        if (typeOperand.isInt() || typeOperand.isFloat()) {
            this.setType(typeOperand);
        } else {
            throw new ContextualError("Minus operation is only allowed for int or float types", this.getLocation());
        }

        LOG.debug("verify UnaryMinus: end");
        return typeOperand;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
