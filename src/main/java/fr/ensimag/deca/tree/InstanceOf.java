package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractExpr expr;
    final private AbstractIdentifier type;

    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify InstanceOf: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        // Get expr type
        Type exprType = this.expr.verifyExpr(compiler, localEnv, currentClass);
        Type instanceType = this.type.verifyType(compiler);

        if (!exprType.isClass() || !instanceType.isClass()) {
            throw new ContextualError("InstanceOf works only with two class types operands", this.getLocation());
        }

        this.setType(instanceType);
        LOG.debug("verify InstanceOf: end");
        return instanceType;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
