package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class Modulo extends AbstractOpArith {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Modulo: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (typeLeftOp.isInt() && typeRightOp.isInt()) {
            // Case : (int, int) for modulo
            this.setType(typeLeftOp);
        } else {
            throw new ContextualError("Modulo operation is only allowed for int type", this.getLocation());
        }

        LOG.debug("verify Modulo: end");
        return typeLeftOp;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
