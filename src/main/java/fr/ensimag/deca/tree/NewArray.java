package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.Iterator;

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

    public void decompile(IndentPrintStream s){
        s.print("new ");
        type.decompile(s);
        // Code of ListExpr printed, with brackets added.
        for (AbstractExpr abstractExpr : indexList.getList()) {
            s.print("[");
            abstractExpr.decompile(s);
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

    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify NewArray: start");

        // Check array type (int or float):
        Type arrayPrimitiveType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create(type.getName().getName())).getType();
        if(!(arrayPrimitiveType.isFloat() || arrayPrimitiveType.isInt())){
            throw new ContextualError("An array must contain float or int", this.getLocation());
        }
        this.setType(arrayPrimitiveType);

        // Check that every index is an int
        for (AbstractExpr abstractExpr : indexList.getList()) {
            Type currentType = abstractExpr.verifyExpr(compiler, localEnv, currentClass);
            if (!currentType.isInt()) {
                throw new ContextualError("Index of array must be an integer", this.getLocation());
            }
        }

        Type returnType;
        if(indexList.getList().size() == 1){
            if(arrayPrimitiveType.isInt()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int[]")).getType();
            }else if(arrayPrimitiveType.isFloat()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float[]")).getType();
            }else{
                throw new ContextualError("An array must contain float or int", this.getLocation());
            }
        }else if(indexList.getList().size() == 2){
            if(arrayPrimitiveType.isInt()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int[][]")).getType();
            }else if(arrayPrimitiveType.isFloat()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float[][]")).getType();
            }else{
                throw new ContextualError("An array must contain float or int", this.getLocation());
            }
        }else{
            throw new ContextualError("The dimension of an array cannot be greater than 2", this.getLocation());
        }

        LOG.debug("verify NewArray: end");
        return returnType;
    }

}
