package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    /**
     * Generate code that sends to "branch" if "this" is evaluated to "bool", then load evaluation of "this" to Rn
     *
     * @param compiler
     * @param bool
     * @param branch
     * @param n
     */
    protected void codeGenExpr(DecacCompiler compiler, boolean bool, Label branch, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        Label continueBranch = new Label(compiler.getLabelGenerator().generateLabel("continue"));
        // Generate code that sends to "branch" if "this" is evaluated to "bool"
        this.codeGenExprBool(compiler, bool, branch, n);
        // If "this" is not evaluated to "bool", load !bool and jump to continue
        int intValue = 0;
        if (bool) {
            intValue = 1;
        }
        compiler.addInstruction(new LOAD(new ImmediateInteger(1 - intValue), Register.getR(n)));
        compiler.addInstruction(new BRA(continueBranch));
        // If "this" is evaluated to "bool", load bool
        compiler.addLabel(branch);
        compiler.addInstruction(new LOAD(new ImmediateInteger(intValue), Register.getR(n)));
        // Generate label continue
        compiler.addLabel(continueBranch);
    }

    /**
     * Generate code so that the boolean evaluation of the binary expression is loaded into Rn
     *
     * @param compiler
     * @param n
     */
    protected void codeGenExprBool(DecacCompiler compiler, int n) {
        Label trueBranch = new Label(compiler.getLabelGenerator().generateLabel("boolIsTrue"));
        codeGenExpr(compiler, true, trueBranch, n);
    }

    /**
     * Calculate right operand expression, and load it into R(n+1) or R(0)
     *
     * @param compiler
     * @param n
     * @return register number where right operand is loaded
     */
    protected int codeGenExprRightOperand(DecacCompiler compiler, int n) {
        int maxRegister = compiler.setAndVerifyCurrentRegister(n);

        if (n < maxRegister) {
            this.getRightOperand().codeGenExpr(compiler, n+1);
            return n+1;
        } else {
            compiler.incTempStackCurrent(1);
            compiler.setTempStackMax();
            compiler.addInstruction(new PUSH(Register.getR(n)), "save");
            this.getRightOperand().codeGenExpr(compiler, n);
            compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
            compiler.addInstruction(new POP(Register.getR(n)), "restore");
            compiler.incTempStackCurrent(-1);
            return 0;
        }
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

}
