package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
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
        Type returnType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("boolean")).getType();

        if (!((exprType.isClass() || exprType.isNull()) && !instanceType.isClass())) {
            throw new ContextualError("InstanceOf works only with two class types operands", this.getLocation());
        }

        this.setType(returnType);
        LOG.debug("verify InstanceOf: end");
        return returnType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        expr.decompile(s);
        s.print(" instanceof ");
        type.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        type.iter(f);
    }
}
