package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public abstract class AbstractMethodBody extends Tree {

    /**
     * Implements non-terminal "method_body" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler Contains "env_types" attribute
     * @param localEnv Corresponds to "env_exp" attribute
     * @param currentClass Corresponds to "class" attribute
     * @param returnType Corresponds to "return" attribute
     */
    protected abstract void verifyMethodBody(
            DecacCompiler compiler,
            EnvironmentExp localEnv,
            ClassDefinition currentClass,
            Type returnType)
            throws ContextualError;

    /**
     * Generate assembly code for the method declaration (pass 2 of [Gencode])
     * Generate the method's declarations and instructions
     *
     * @param compiler Deca Compiler used to add IMA instructions
     * @param localEnv Corresponds to the "env_exp" attribute
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv);
}
