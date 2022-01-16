package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

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
