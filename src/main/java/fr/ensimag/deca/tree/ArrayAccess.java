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
                           EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify BooleanLiteral: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        // Validate.notNull(localEnv, "Env_exp object should not be null");

        Type indexType = index.verifyExpr(compiler, localEnv, currentClass);
        Type tabType = tab.verifyExpr(compiler, localEnv, currentClass);
        Type returnType;

        // Verify that the expression index is an integer
        if(!indexType.isInt()){
            throw new ContextualError("The index of an ArrayAccess must be an integer", this.getLocation());
        } else {
            // If 'tab' is a matrix, returns an access to one of its component, a vector of the same type.
            if (tabType.isMatrixFloat()) {
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float[]")).getType();
                this.setType(returnType);
            } else if (tabType.isMatrixInt()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int[]")).getType();
                this.setType(returnType);
                // If 'tab' is a vector, returns an access to one of its component, a literal of the same type.
            } else if (tabType.isVectorFloat()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float")).getType();
                this.setType(returnType);
            } else if (tabType.isVectorInt()){
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int")).getType();
                this.setType(returnType);
            } else {
                throw new ContextualError("Identifier for array access must be a 1D or 2D array of float or int.", this.getLocation());
            }
        }

        LOG.debug("verify BooleanLiteral: start");
        return returnType;
    }

    @Override
    protected void codeGenStore(DecacCompiler compiler, int n) {
        return;
    }
}
