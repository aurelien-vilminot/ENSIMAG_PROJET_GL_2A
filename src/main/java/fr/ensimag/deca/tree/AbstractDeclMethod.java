package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {

    // TODO: comment
    protected abstract void verifyDeclMethod(DecacCompiler compiler,
                                             SymbolTable.Symbol superSymbol,
                                             SymbolTable.Symbol classSymbol)
            throws ContextualError;

    protected abstract void verifyMethodBody(DecacCompiler compiler, SymbolTable.Symbol classSymbol)
            throws ContextualError;

    /**
     * Generate assembly code for the method declaration (pass 2 of [Gencode])
     *
     * @param compiler
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler);


    /**
     * Generate assembly code to build the virtual methods table (pass 1 of [Gencode])
     *
     * @param compiler
     */
    protected abstract void codeGenMethodTable(DecacCompiler compiler, AbstractIdentifier className);
}
