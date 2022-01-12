package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ReadFloat extends AbstractReadExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify ReadFloat: start");

        Type floatType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float")).getType();
        this.setType(floatType);

        LOG.debug("verify ReadFloat: end");
        return floatType;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
