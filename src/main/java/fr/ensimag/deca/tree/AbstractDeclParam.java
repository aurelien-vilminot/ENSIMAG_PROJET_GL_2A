package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public abstract class AbstractDeclParam extends Tree {
    // TODO: comment
    protected abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;

    // TODO: comment
    protected abstract void verifyParamEnvExp(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;

}
