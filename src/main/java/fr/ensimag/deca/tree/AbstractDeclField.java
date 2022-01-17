package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree{

    // TODO: comment, pass2
    protected abstract void verifyDeclField(DecacCompiler compiler,
                                            SymbolTable.Symbol superSymbol, SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError;

    // TODO: comment, pass3
    protected abstract void verifyInitField(DecacCompiler compiler, SymbolTable.Symbol currentClass)
            throws ContextualError;

    /**
     * Generate assembly code for the field declaration.
     *
     * @param compiler
     */
    protected abstract void codeGenDeclField(DecacCompiler compiler);
}
