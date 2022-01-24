package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Assign: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        // Get lvalue type
        Type expectedType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);

        // Check rvalue type
        this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, expectedType);
        this.setType(expectedType);

        if (this.getLeftOperand().getType().isFloat() && this.getRightOperand().getType().isInt()) {
            // Implicit float conversion
            this.setRightOperand(new ConvFloat(this.getRightOperand()));
            this.getRightOperand().setType(this.getLeftOperand().getType());
        }

        LOG.debug("verify Assign: end");

        return expectedType;
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch, int n) {
        Validate.isTrue(this.getType().isBoolean());
        compiler.setAndVerifyCurrentRegister(n);

        codeGenInst(compiler, n);
        this.getLeftOperand().codeGenExprBool(compiler, bool, branch, n);
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        codeGenInst(compiler, n);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenInst(compiler, 2);
        // Registers are no longer used
        compiler.setAndVerifyCurrentRegister(0);
    }

    /**
     * Generate assembly code for the instruction, and store the result in Rn
     *
     * @param compiler Deca Compiler used to add IMA instructions
     * @param n Register number
     */
    protected void codeGenInst(DecacCompiler compiler, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        // Calculate rightOperand and load into Rn
        getRightOperand().codeGenExpr(compiler, n);
        // Store rightOperand into leftOperand
        getLeftOperand().codeGenStore(compiler, n);
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
