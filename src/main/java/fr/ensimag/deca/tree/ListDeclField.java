package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.log4j.Logger;

public class ListDeclField extends TreeList<AbstractDeclField> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    protected void verifyListDeclField(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError {
        LOG.debug("verify listDeclField: start");
        for (AbstractDeclField abstractDeclField : this.getList()) {
            abstractDeclField.verifyDeclField(compiler, superSymbol, symbolCurrentClass);
        }
        ((ClassDefinition) compiler.getEnvironmentTypes().get(symbolCurrentClass)).setNumberOfFields(this.getList().size());
        LOG.debug("verify listDeclField: end");
    }

    protected void verifyListInitField(DecacCompiler compiler, SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError {
        LOG.debug("verify listInitField: start");
        for (AbstractDeclField abstractDeclField : this.getList()) {
            abstractDeclField.verifyInitField(compiler, symbolCurrentClass);
        }
        LOG.debug("verify listInitField: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
    }

    protected void codeGenListDeclField(DecacCompiler compiler) {
        for (AbstractDeclField c : getList()) {
            c.codeGenDeclField(compiler);
        }
    }
}
