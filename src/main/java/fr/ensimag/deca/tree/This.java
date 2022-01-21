package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class This extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private final boolean impl;

    public This(boolean impl) {
        this.impl = impl;
    }

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
    boolean isImplicit() {
        return impl;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (!impl) {
            s.print("this");
        }
    }

    @Override
    public DVal dval(DecacCompiler compiler) {
        // Return heap address
        return new RegisterOffset(-2, Register.LB);
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
