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

// TODO : Verify the extends
public class ArrayAccess extends AbstractLValue{
    private final AbstractExpr tab;
    private final AbstractExpr index;

    public ArrayAccess(AbstractExpr tab, AbstractExpr index){
            Validate.notNull(tab);
            Validate.notNull(index);
            this.tab = tab;
            this.index = index;
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
