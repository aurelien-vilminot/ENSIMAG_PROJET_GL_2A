package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
            Validate.notNull(localEnv, "Env_exp object should not be null");

            Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            Type returnType;

            if ((typeLeftOp.isInt() && typeRightOp.isInt()) || (typeLeftOp.isFloat() && typeRightOp.isInt())) {
                // Cases : (int, int) and (float, int)
                this.setType(typeLeftOp);
                returnType = typeLeftOp;
            } else if (typeRightOp.isFloat()) {
                // Cases : (int, float) and (float, float)
                this.setType(typeRightOp);
                returnType = typeRightOp;
            } else {
                throw new ContextualError("Binary operation is only allowed for int or float types", this.getLocation());
            }

            LOG.debug("verify OpArith: end");
            return returnType;
    }
}
