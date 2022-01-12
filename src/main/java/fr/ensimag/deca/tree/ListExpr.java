package fr.ensimag.deca.tree;

import java.util.Iterator;
import java.util.List;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
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
