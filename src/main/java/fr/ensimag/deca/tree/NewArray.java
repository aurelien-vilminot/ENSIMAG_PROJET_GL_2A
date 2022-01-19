package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import sun.jvm.hotspot.types.WrongTypeException;

import java.io.PrintStream;
import java.util.Iterator;

// TODO : Trouver la classe dont elle hérite.
public class NewArray extends AbstractExpr{
    private static final Logger LOG = Logger.getLogger(Main.class);
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
        s.print("new ");
        type.decompile(s);
        // Code of ListExpr copied, with brackets added.
        Iterator<AbstractExpr> ite = indexList.getList().iterator();
        if (ite.hasNext()) {
            s.print("[");
            ite.next().decompile(s);
            s.print("]");
        }
        while (ite.hasNext()) {
            s.print("[");
            ite.next().decompile(s);
            s.print("]");
        }
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        indexList.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        indexList.prettyPrint(s, prefix, false);
    }




    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass){
        LOG.debug("verify NewArray: start");

        // Check array type (int or float):
        Type arrayPrimitiveType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create(type.getName().getName())).getType();
        if(!(arrayPrimitiveType.isFloat() || arrayPrimitiveType.isInt())){
            throw new WrongTypeException("Type of arrays must be int or float.");
        }
        this.setType(arrayPrimitiveType);

        // Check that every index is an int
        // TODO cast float to int ?? or not ...
        indexList.getList().get(0).getType();
        Iterator<AbstractExpr> ite = indexList.getList().iterator();
        while (ite.hasNext()) {
            Type currentType = ite.next().getType();
            if (!currentType.isInt()) {
                throw new WrongTypeException("Index of array must be int.");
            }
        }
        // now, we sure that all index are int !

        Type returnType;
        if(indexList.getList().size() == 1){
            if(arrayPrimitiveType.isInt()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int[]")).getType();
            }else if(arrayPrimitiveType.isFloat()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float[]")).getType();
            }else{
                throw new WrongTypeException("Type of arrays must be int or float.");
            }
        }else if(indexList.getList().size() == 2){
            if(arrayPrimitiveType.isInt()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int[][]")).getType();
            }else if(arrayPrimitiveType.isFloat()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float[][]")).getType();
            }else{
                throw new WrongTypeException("Type of arrays must be int or float.");
            }
        }else{
            // TODO Change exception
            throw new WrongTypeException("Dimensions higher than 2 have not been implemented.");
        }
        LOG.debug("verify NewArray: end");
        return returnType;
    }

}
