package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Not: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);

        if (typeOperand.isBoolean()) {
            this.setType(typeOperand);
        } else {
            throw new ContextualError("Not operation is only allowed for boolean type", this.getLocation());
        }

        LOG.debug("verify Not: end");
        return typeOperand;
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch) {
        getOperand().codeGenExprBool(compiler, !bool, branch);
    }
}
