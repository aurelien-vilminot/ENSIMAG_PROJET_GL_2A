package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

// TODO : Trouver la classe dont elle hérite.
public class NewArray extends AbstractExpr{
    final private AbstractIdentifier type;
    final private ListExpr indexList;

    public NewArray(AbstractIdentifier type, ListExpr indexList){
        Validate.notNull(type);
        Validate.notNull(indexList);
        this.type = type;
        this.indexList = indexList;
    }

    // TODO
    public void decompile(IndentPrintStream s){

    }

    protected void iterChildren(TreeFunction f){

    }

    protected void prettyPrintChildren(PrintStream s, String prefix){

    }




    public Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass){
        return null;
    }

}
