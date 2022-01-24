package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public abstract class AbstractDeclParam extends Tree {

    /**
     * Implements non-terminal "DeclParam" of [SyntaxeContextuelle] in pass 2
     *
     * @param compiler Contains "env_types" attribute
     */
    protected abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;

    /**
     * Implements non-terminal "DeclParam" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler Contains "env_types" attribute
     */
    protected abstract void verifyParamEnvExp(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;

    /**
     * Generate assembly code for the method declaration (pass 2 of [Gencode])
     * Sets the operand of each parameter in the local environment
     *
     * @param compiler Deca Compiler used to add IMA instruction
     * @param localEnv Corresponds to the "env_exp" attribute
     * @param index Parameter index
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, int index);
}
