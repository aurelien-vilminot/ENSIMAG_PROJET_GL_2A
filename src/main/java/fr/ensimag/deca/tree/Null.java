package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class Null extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Program.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Null: start");

        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        NullType nullType = new NullType(compiler.getSymbolTable().create("null"));
        this.setType(nullType);
        LOG.debug("verify Null: end");
        return nullType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // nothing to do
    }
}
