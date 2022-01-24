package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclField extends Tree{

    /**
     * Implements non-terminal "DeclField" of [SyntaxeContextuelle] in pass 2
     *
     * @param compiler Contains "env_types" attribute
     * @param superSymbol Superclass symbol
     * @param symbolCurrentClass Current class symbol
     */
    protected abstract void verifyDeclField(DecacCompiler compiler,
                                            SymbolTable.Symbol superSymbol, SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError;

    /**
     * Implements non-terminal "DeclField" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler Contains "env_types" attribute
     * @param currentClass Current class symbol
     */
    protected abstract void verifyInitField(DecacCompiler compiler, SymbolTable.Symbol currentClass)
            throws ContextualError;

    /**
     * Generate assembly code for the field declaration.
     *
     * @param compiler Deca Compiler used to add IMA instruction
     */
    protected abstract void codeGenDeclFieldDefault(DecacCompiler compiler);

    /**
     * Generate assembly code for the field declaration.
     *
     * @param compiler Deca Compiler used to add IMA instruction
     */
    protected abstract void codeGenDeclField(DecacCompiler compiler);
}
