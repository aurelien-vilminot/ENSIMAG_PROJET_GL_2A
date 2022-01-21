package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

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
        Type arrayPrimitiveType = this.type.verifyType(compiler);
        if (!(arrayPrimitiveType.isFloat() || arrayPrimitiveType.isInt())){
            throw new ContextualError("An array must contain float or int", this.getLocation());
        }
        this.setType(arrayPrimitiveType);

        // Check that every index is an int
        for (AbstractExpr abstractExpr : this.indexList.getList()) {
            Type currentType = abstractExpr.verifyExpr(compiler, localEnv, currentClass);
            if (!currentType.isInt()) {
                throw new ContextualError("Index of array must be an integer", this.getLocation());
            }
        }

        Type returnType;
        int arraySize = this.indexList.getList().size();
        if (arraySize <= 2) {
            // Generate brackets
            StringBuilder brackets = new StringBuilder();
            for (int i = 0 ; i < arraySize ; ++i) {
                brackets.append("[]");
            }

            if (arrayPrimitiveType.isInt()) {
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int" + brackets)).getType();
            } else if (arrayPrimitiveType.isFloat()) {
                returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float" + brackets)).getType();
            } else {
                throw new ContextualError("An array must contain float or int", this.getLocation());
            }
        } else {
            throw new ContextualError("The dimension of an array cannot be greater than 2", this.getLocation());
        }

        LOG.debug("verify NewArray: end");
        return returnType;
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        compiler.addInstruction(new ADDSP(new ImmediateInteger(2)));
        compiler.addInstruction(new PUSH(Register.getR(2)));
        indexList.getList().get(0).codeGenExpr(compiler, 3);
        if (indexList.getList().size() == 1) {
            createEmptyTable(compiler);
        } else if (indexList.getList().size() == 2) {
            createEmptyTableOfTable(compiler);
        } else {
            throw new UnsupportedOperationException("Arrays of dimension >2 are not supported.");
        }
        compiler.addInstruction(new LEA(new RegisterOffset(0, Register.getR(2)), Register.getR(n)));
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.addInstruction(new SUBSP(new ImmediateInteger(2)));
    }

    /**
     * Fill empty table, return its heap address in R2
     * Precondition : size of array is in R3
     *
     * @param compiler
     */
    protected void createEmptyTable(DecacCompiler compiler) {
        compiler.addComment("Array begin");

        // Load size of array in R1
        compiler.addInstruction(new LOAD(Register.getR(3), Register.R1));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), Register.R1));

        // R0, R2 <- heap address of size (length + 1)
        compiler.addInstruction(new NEW(Register.R1, Register.R0));
        compiler.addInstruction(new LEA(new RegisterOffset(0, Register.R0), Register.getR(2)));

        // Store array length inside 0(heap address)
        compiler.addInstruction(new SUB(new ImmediateInteger(1), Register.R1));
        compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(0, Register.R0)));

        // Fill table with zeros
        Label begin_fill = new Label(compiler.getLabelGenerator().generateLabel("begin_fill"));
        Label cond_fill = new Label(compiler.getLabelGenerator().generateLabel("cond_fill"));

        // R3 <- default value
        if (type.getType().isInt()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.getR(3)));
        } else {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.getR(3)));
        }

        // While loop to fill the array with zeros
        compiler.addInstruction(new BRA(cond_fill));
        compiler.addLabel(begin_fill);
        // R0 <- address of 1(R0)
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.R0), Register.R0));
        // 0(R0) <- default value
        compiler.addInstruction(new STORE(Register.getR(3), new RegisterOffset(0, Register.R0)));
        // size -= 1
        compiler.addInstruction(new SUB(new ImmediateInteger(1), Register.R1));

        compiler.addLabel(cond_fill);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R1));
        compiler.addInstruction(new BNE(begin_fill));

        compiler.addComment("Array end");
    }

    /**
     * Fill an empty table of tables, return its heap address in R2
     * Precondition : size of this empty table is in R3
     *
     * @param compiler
     */
    protected void createEmptyTableOfTable(DecacCompiler compiler) {
        compiler.addComment("Matrix begin");
        compiler.addInstruction(new ADDSP(new ImmediateInteger(4)));

        // Load size of array in R1
        compiler.addInstruction(new LOAD(Register.getR(3), Register.R1));
        compiler.addInstruction(new ADD(new ImmediateInteger(1), Register.R1));

        // R0 <- heap address of size (length + 1)
        compiler.addInstruction(new NEW(Register.R1, Register.R0));
        compiler.addInstruction(new PUSH(Register.R0));

        // Store array length inside 0(heap address)
        compiler.addInstruction(new SUB(new ImmediateInteger(1), Register.R1));
        compiler.addInstruction(new STORE(Register.R1, new RegisterOffset(0, Register.R0)));

        // R3 <- size of the empty tables to generate inside
        compiler.addInstruction(new PUSH(Register.R0));
        indexList.getList().get(1).codeGenExpr(compiler, 3);
        compiler.addInstruction(new POP(Register.R0));

        // Fill table with empty tables
        Label begin_fill = new Label(compiler.getLabelGenerator().generateLabel("begin_fill"));
        Label cond_fill = new Label(compiler.getLabelGenerator().generateLabel("cond_fill"));

        // While loop to fill the array with zeros
        compiler.addInstruction(new BRA(cond_fill));
        compiler.addLabel(begin_fill);
        compiler.addInstruction(new LEA(new RegisterOffset(1, Register.R0), Register.R0));

        // R2 <- heap address of the empty table created (erase R0, R1, R2)
        compiler.addInstruction(new PUSH(Register.R0));
        compiler.addInstruction(new PUSH(Register.R1));
        compiler.addInstruction(new PUSH(Register.getR(3)));
        createEmptyTable(compiler);
        compiler.addInstruction(new POP(Register.getR(3)));
        compiler.addInstruction(new POP(Register.R1));
        compiler.addInstruction(new POP(Register.R0));
        // 0(R0) <- R2
        compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(0, Register.R0)));
        // size -= 1
        compiler.addInstruction(new SUB(new ImmediateInteger(1), Register.R1));

        compiler.addLabel(cond_fill);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R1));
        compiler.addInstruction(new BNE(begin_fill));

        // Restore erased register
        compiler.addInstruction(new POP(Register.getR(2)));
        compiler.addInstruction(new SUBSP(new ImmediateInteger(4)));
        compiler.addComment("Matrix end");
    }
}
