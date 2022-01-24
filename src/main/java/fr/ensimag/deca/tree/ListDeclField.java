package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    /**
     * Implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 2
     *
     * @param compiler Contains the "env_types" attribute
     * @param superSymbol Super-class symbol
     * @param symbolCurrentClass Current class symbol
     */
    protected void verifyListDeclField(DecacCompiler compiler,
                                       SymbolTable.Symbol superSymbol,
                                       SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError {
        LOG.debug("verify listDeclField: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(symbolCurrentClass, "Symbol of current class should not be null");
        Validate.notNull(superSymbol, "Symbol of super class should not be null");

        for (AbstractDeclField abstractDeclField : this.getList()) {
            abstractDeclField.verifyDeclField(compiler, superSymbol, symbolCurrentClass);
        }
        LOG.debug("verify listDeclField: end");
    }

    /**
     * Implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler Contains the "env_types" attribute
     * @param symbolCurrentClass Symbol of the current class
     */
    protected void verifyListInitField(DecacCompiler compiler, SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError {
        LOG.debug("verify listInitField: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(symbolCurrentClass, "Symbol of current class should not be null");

        for (AbstractDeclField abstractDeclField : this.getList()) {
            abstractDeclField.verifyInitField(compiler, symbolCurrentClass);
        }
        LOG.debug("verify listInitField: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (getList().isEmpty()) {
            return;
        }
        for (AbstractDeclField v : getList()) {
            v.decompile(s);
            s.println();
        }
    }

    /**
     * Initialize fields address, and store default value if superClass has fields
     *
     * @param compiler Deca Compiler used to add IMA instructions
     */
    protected void codeGenListDeclFieldDefault(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        for (AbstractDeclField c : getList()) {
            c.codeGenDeclFieldDefault(compiler);
        }
    }

    /**
     * Initialize fields
     *
     * @param compiler
     */
    protected void codeGenListDeclField(DecacCompiler compiler) {
        for (AbstractDeclField c : getList()) {
            c.codeGenDeclField(compiler);
        }
    }
}
