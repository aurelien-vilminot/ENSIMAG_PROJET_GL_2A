package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
