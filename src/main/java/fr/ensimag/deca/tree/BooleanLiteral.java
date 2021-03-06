package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;
    private static final Logger LOG = Logger.getLogger(Main.class);

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify BooleanLiteral: start");

        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        Type booleanType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("boolean")).getType();
        this.setType(booleanType);

        LOG.debug("verify BooleanLiteral: end");
        return booleanType;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    public DVal dval(DecacCompiler compiler) {
        int intValue = 0;
        if (value) {
            intValue = 1;
        }
        return new ImmediateInteger(intValue);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

}
