package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.ima.pseudocode.Instruction;

public class ExpressionAtomique {
    public static String dval(IntLiteral n) {
        return "#" + n.getValue();
    }

    public static String dval(DecacCompiler compiler, Identifier symb) {
        //TODO
        return "@";
    }
}
