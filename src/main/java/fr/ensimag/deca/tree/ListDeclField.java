package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    protected void verifyListDeclField(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol symbolCurrentClass)
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

    protected void codeGenListDeclField(DecacCompiler compiler) {
        for (AbstractDeclField c : getList()) {
            c.codeGenDeclField(compiler);
        }
    }
}
