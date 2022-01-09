package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl07
 * @date 01/01/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify ConvFloat: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);

        if (typeOperand.isFloat()) {
            this.setType(typeOperand);
        } else {
            throw new ContextualError("An int can be cast in float only", this.getLocation());
        }

        LOG.debug("verify ConvFloat: end");
        return typeOperand;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
