package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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

        Type typeOperand = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeFloat = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float")).getType();

        if (typeOperand.isInt()) {
            this.setType(typeFloat);
        } else {
            throw new ContextualError("Implicit cast works only with int to float", this.getLocation());
        }

        LOG.debug("verify ConvFloat: end");
        return typeFloat;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        getOperand().codeGenExpr(compiler, n);
        compiler.addInstruction(new FLOAT(Register.getR(n), Register.getR(n)));
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
