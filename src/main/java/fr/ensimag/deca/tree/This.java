package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class This extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify This: start");

        if (currentClass == null || !currentClass.getType().isClass()) {
            throw new ContextualError("Impossible to use 'this' identifier in the main program", this.getLocation());
        }

        this.setType(currentClass.getType());
        LOG.debug("verify This: end");
        return currentClass.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
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
