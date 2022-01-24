package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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

    @Override
    public void decompile(IndentPrintStream s){
        tab.decompile(s);
        s.print("[");
        index.decompile(s);
        s.print("]");
    }

    @Override
    protected void iterChildren(TreeFunction f){
        tab.iter(f);
        index.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix){
        tab.prettyPrint(s, prefix, false);
        index.prettyPrint(s, prefix, true);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify ArrayAccess: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        // Validate.notNull(localEnv, "Env_exp object should not be null");

        Type indexType = index.verifyExpr(compiler, localEnv, currentClass);
        Type tabType = tab.verifyExpr(compiler, localEnv, currentClass);
        Type returnType;

        // Verify that the expression index is an integer
        if (!indexType.isInt()){
            throw new ContextualError("The index for array access must be an integer", this.getLocation());
        }

        if (tabType.isVectorFloat() || tabType.isVectorInt() || tabType.isMatrixFloat() || tabType.isMatrixInt()) {
            String stringTabType = tabType.toString().substring(0, tabType.toString().length() - 2);
            returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create(stringTabType)).getType();
        } else {
            throw new ContextualError(
                    "Identifier for array access must be a 1D or 2D array of float or int",
                    this.getLocation()
            );
        }

        this.setType(returnType);
        LOG.debug("verify ArrayAccess: end");
        return returnType;
    }

    /**
     * Load into R0 the address of tab[index]
     * Precondition: R0 = heap address of array ; Rn = index
     *
     * @param compiler
     * @param n
     */
    protected void codeGenAddress(DecacCompiler compiler, int n) {
        // Iterate through array
        Label begin_access = new Label(compiler.getLabelGenerator().generateLabel("begin_access"));
        Label cond_access = new Label(compiler.getLabelGenerator().generateLabel("cond_access"));

        // While loop to fill the array with zeros
        compiler.addInstruction(new BRA(cond_access));
        compiler.addLabel(begin_access);
        // R0 <- address of 1(R0)
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.R0), Register.R0));
        // size -= 1
        compiler.addInstruction(new SUB(new ImmediateInteger(1), Register.getR(n)));

        compiler.addLabel(cond_access);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(n)));
        compiler.addInstruction(new BNE(begin_access));
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        int maxRegister = compiler.setAndVerifyCurrentRegister(n);

        // Rn <- heap address of array
        tab.codeGenExpr(compiler, n);
        compiler.addInstruction(new LEA(new RegisterOffset(0, Register.getR(n)), Register.R0));
        // R0 <- heap address of index 0
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.R0), Register.R0));

        // Rn <- index
        compiler.incTempStackCurrent(1);
        compiler.addInstruction(new PUSH(Register.R0));
        index.codeGenExpr(compiler, n);
        compiler.addInstruction(new POP(Register.R0));
        compiler.incTempStackCurrent(-1);

        // Verify index >= 0
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(n)));
        compiler.addInstruction(new BLT(compiler.getLabelGenerator().getIndexOutOfBoundsLabel()));
        // Verify index < -1(R0)
        if (n < maxRegister) {
            compiler.addInstruction(new LOAD(new RegisterOffset(-1, Register.R0), Register.getR(n+1)));
            compiler.addInstruction(new CMP(Register.getR(n), Register.getR(n+1)));
        } else {
            compiler.incTempStackCurrent(1);
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new LOAD(new RegisterOffset(-1, Register.R0), Register.R0));
            compiler.addInstruction(new CMP(Register.getR(n), Register.R0));
            compiler.addInstruction(new POP(Register.R0));
            compiler.incTempStackCurrent(-1);
        }
        compiler.addInstruction(new BLE(compiler.getLabelGenerator().getIndexOutOfBoundsLabel()));


        // R0 <- address of tab[index]
        codeGenAddress(compiler, n);

        // Rn <- value of tab[index]
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R0), Register.getR(n)));
    }

    @Override
    protected void codeGenStore(DecacCompiler compiler, int n) {
        int maxRegister = compiler.setAndVerifyCurrentRegister(n);

        // Rn = value to store inside tab[index]
        compiler.incTempStackCurrent(1);
        compiler.addInstruction(new PUSH(Register.getR(n)));

        // Rn <- heap address of array
        tab.codeGenExpr(compiler, n);
        compiler.addInstruction(new LEA(new RegisterOffset(0, Register.getR(n)), Register.R0));
        // R0 <- heap address of index 0
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.R0), Register.R0));

        // Rn <- index
        compiler.incTempStackCurrent(1);
        compiler.addInstruction(new PUSH(Register.R0));
        index.codeGenExpr(compiler, n);
        compiler.addInstruction(new POP(Register.R0));
        compiler.incTempStackCurrent(-1);

        // Verify index >= 0
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(n)));
        compiler.addInstruction(new BLT(compiler.getLabelGenerator().getIndexOutOfBoundsLabel()));
        // Verify index < -1(R0)
        if (n < maxRegister) {
            compiler.addInstruction(new LOAD(new RegisterOffset(-1, Register.R0), Register.getR(n+1)));
            compiler.addInstruction(new CMP(Register.getR(n), Register.getR(n+1)));
        } else {
            compiler.incTempStackCurrent(1);
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new LOAD(new RegisterOffset(-1, Register.R0), Register.R0));
            compiler.addInstruction(new CMP(Register.getR(n), Register.R0));
            compiler.addInstruction(new POP(Register.R0));
            compiler.incTempStackCurrent(-1);
        }
        compiler.addInstruction(new BLE(compiler.getLabelGenerator().getIndexOutOfBoundsLabel()));

        // R0 <- address of tab[index]
        codeGenAddress(compiler, n);

        // Rn = value to store inside tab[index]
        compiler.addInstruction(new POP(Register.getR(n)));
        compiler.incTempStackCurrent(-1);

        // 0(R0) <- value of Rn
        compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(0, Register.getR(0))));
    }
}
