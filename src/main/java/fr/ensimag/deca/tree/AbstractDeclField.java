package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclField extends Tree{

    // TODO: comment
    protected abstract void verifyDeclField(DecacCompiler compiler,
                                          EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Generate assembly code for the field declaration.
     *
     * @param compiler
     */
    protected abstract void codeGenDeclField(DecacCompiler compiler);
}
