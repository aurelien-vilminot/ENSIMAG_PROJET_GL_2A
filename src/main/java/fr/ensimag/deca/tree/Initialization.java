package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl07
 * @date 01/01/2022
 */
public class Initialization extends AbstractInitialization {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify Initialization: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        Type type = this.expression.verifyRValue(compiler, localEnv, currentClass, t).getType();
        this.expression.setType(type);

        if (t.isFloat() && this.expression.getType().isInt()) {
            // Implicit float conversion
            this.expression = new ConvFloat(this.expression);
            this.expression.setType(t);
        }

        LOG.debug("verify Initialization: end");
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr dAddr) {
        expression.codeGenExpr(compiler, 2);
        compiler.addInstruction(new STORE(Register.getR(2), dAddr));
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n, Type type) {
        expression.codeGenExpr(compiler, n);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
