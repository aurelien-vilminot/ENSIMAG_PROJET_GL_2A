package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {

    // TODO: comment
    protected abstract void verifyDeclMethod(DecacCompiler compiler,
                                             SymbolTable.Symbol superSymbol)
            throws ContextualError;

    /**
     * Generate assembly code for the method declaration.
     *
     * @param compiler
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler);
}
