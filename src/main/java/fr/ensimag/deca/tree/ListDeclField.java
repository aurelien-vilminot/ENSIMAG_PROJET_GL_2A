package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {

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
