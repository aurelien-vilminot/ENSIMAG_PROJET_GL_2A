package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable;

public abstract class AbstractDeclMethod extends Tree {

    /**
     * Implements non-terminal "DeclMethod" of [SyntaxeContextuelle] in pass 2
     *
     * @param compiler Contains "env_types" attribute
     * @param superSymbol Superclass symbol
     * @param classSymbol Current class symbol
     */
    protected abstract void verifyDeclMethod(DecacCompiler compiler,
                                             SymbolTable.Symbol superSymbol,
                                             SymbolTable.Symbol classSymbol)
            throws ContextualError;

    /**
     * Implements non-terminal "DeclMethod" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler Contains "env_types" attribute
     * @param classSymbol Current class symbol
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler, SymbolTable.Symbol classSymbol)
            throws ContextualError;

    /**
     * Generate assembly code for the method declaration (pass 2 of [Gencode])
     *
     * @param compiler Deca Compiler used to add IMA instruction
     */
    protected abstract void codeGenDeclMethod(DecacCompiler compiler);


    /**
     * Generate assembly code to build the virtual methods table (pass 1 of [Gencode])
     * Construct the labelArrayList for the current class
     *
     * @param compiler Deca Compiler used to add IMA instruction
     * @param className Identifier of the class
     */
    protected abstract void codeGenMethodTable(DecacCompiler compiler, AbstractIdentifier className);
}
