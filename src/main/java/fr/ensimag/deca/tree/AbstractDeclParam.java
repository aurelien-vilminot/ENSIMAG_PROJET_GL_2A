package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public abstract class AbstractDeclParam extends Tree {
    // TODO: comment
    protected abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;

    // TODO: comment
    protected abstract void verifyParamEnvExp(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;

    /**
     * Generate assembly code for the method declaration (pass 2 of [Gencode])
     * Sets the operand of each parameter in the local environment
     *
     * @param compiler
     * @param localEnv
     * @param index
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, int index);
}
