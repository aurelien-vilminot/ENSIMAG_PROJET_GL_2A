package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Single precision, floating-point literal
 *
 * @author gl07
 * @date 01/01/2022
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;
    private static final Logger LOG = Logger.getLogger(Main.class);

    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify FloatLiteral: start");

        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        Type floatType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("float")).getType();
        this.setType(floatType);

        LOG.debug("verify FloatLiteral: end");
        return floatType;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(dval(compiler), Register.R1));
        compiler.addInstruction(new WFLOAT());
    }

    @Override
    protected void codeGenPrintx(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(dval(compiler), Register.R1));
        compiler.addInstruction(new WFLOATX());
    }

    @Override
    public DVal dval(DecacCompiler compiler) {
        return new ImmediateFloat(value);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
