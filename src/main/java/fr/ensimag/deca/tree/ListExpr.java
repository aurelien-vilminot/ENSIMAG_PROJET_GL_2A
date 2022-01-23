package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractExpr> ite = getList().iterator();
        if (ite.hasNext()) {
            ite.next().decompile(s);
        }
        while (ite.hasNext()) {
            s.print(", ");
            ite.next().decompile(s);
        }
    }
}
