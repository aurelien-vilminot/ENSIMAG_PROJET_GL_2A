package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify OPBool: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type typeLeftOp = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeRightOp = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type returnType;

        if ((typeLeftOp.isBoolean() && typeRightOp.isBoolean())) {
            // Case : (boolean, boolean)
            this.setType(typeLeftOp);
            returnType = typeLeftOp;
        } else {
            throw new ContextualError("Boolean operation is only allowed for boolean type", this.getLocation());
        }

        LOG.debug("verify OPBool: end");
        return returnType;
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch, int n) {
        int maxRegister = compiler.getCompilerOptions().getRegisterNumber() - 1;
        Validate.isTrue((n <= maxRegister));

        DVal dval = this.dval(compiler);
        if (dval != null && bool) {
        // If "this" is a known literal
            compiler.addInstruction(new BRA(branch));
        } else {
            switch (this.getOperatorName()) {
                case "&&":
                    // Else calculate left and right operand with lazy branch evaluation
                    Label endBranch = new Label(compiler.getLabelGenerator().generateLabel(branch.toString()) + "_fin");
                    this.getLeftOperand().codeGenExprBool(compiler, false, bool ? endBranch : branch, n);
                    // No need to store inside n+1, as it is evaluated through branches
                    this.getRightOperand().codeGenExprBool(compiler, bool, branch, n);
                    if (bool) {
                        compiler.addLabel(endBranch);
                    }
                    break;
                case "||":
                    // Transform "||" into "&&" with boolean operations
                    Not newExpr = new Not(new And(new Not(this.getLeftOperand()), new Not(this.getRightOperand())));
                    newExpr.codeGenExprBool(compiler, bool, branch, n);
                    break;
            }
        }
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        super.codeGenExprBool(compiler, n);
    }
}
