package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.log4j.Logger;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    protected void verifyListDeclMethod(DecacCompiler compiler, SymbolTable.Symbol superSymbol)
            throws ContextualError {
        LOG.debug("verify listDeclMethod: start");
        for (AbstractDeclMethod abstractDeclMethod : this.getList()) {
            abstractDeclMethod.verifyDeclMethod(compiler, superSymbol);
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
