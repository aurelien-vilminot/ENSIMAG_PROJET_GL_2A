package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import sun.jvm.hotspot.types.WrongTypeException;

import java.io.PrintStream;

// TODO : Verify the extends
public class ArrayAccess extends AbstractLValue{
    private final AbstractExpr tab;
    private final AbstractExpr index;
    private static final Logger LOG = Logger.getLogger(Main.class);

    public ArrayAccess(AbstractExpr tab, AbstractExpr index){
            Validate.notNull(tab);
            Validate.notNull(index);
            this.tab = tab;
            this.index = index;
    }
    // TODO
    public void decompile(IndentPrintStream s){
        tab.decompile(s);
        s.print("[");
        index.decompile(s);
        s.print("]");
    }

    protected void iterChildren(TreeFunction f){
        tab.iter(f);
        index.iter(f);
    }

    protected void prettyPrintChildren(PrintStream s, String prefix){
        tab.prettyPrint(s, prefix, false);
        index.prettyPrint(s, prefix, false);
    }




    public Type verifyExpr(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass){
        LOG.debug("verify BooleanLiteral: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        if(!index.getType().isInt()){
            throw new WrongTypeException("The index of an ArrayAccess must be an integer");
        } else {

            if (tab.getType().isMatrixFloat()) {

            } else if (tab.getType().isVectorFloat()) {

            } else if (tab.getType().isMatrixInt()){

            } else if (tab.getType().isVectorInt()){

            } else {
                throw new WrongTypeException("Attribut 'tab' must be a Vector or a Matrix of Int or Float");
            }

                Type elementAccessed = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("boolean")).getType();
        }
        throw new WrongTypeException("Attribut 'tab' must be a Vector or a Matrix of Int or Float");
    }
}
