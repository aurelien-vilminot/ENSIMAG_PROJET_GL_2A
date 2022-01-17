package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    protected void verifyListDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol classSymbol)
            throws ContextualError {
        LOG.debug("verify listDeclMethod: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(superSymbol, "Symbol of super class should not be null");

        for (AbstractDeclMethod abstractDeclMethod : this.getList()) {
            abstractDeclMethod.verifyDeclMethod(compiler, superSymbol, classSymbol);
        }
        LOG.debug("verify listDeclMethod: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
    }

    protected void codeGenListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod c : getList()) {
            c.codeGenDeclMethod(compiler);
        }
    }
}
