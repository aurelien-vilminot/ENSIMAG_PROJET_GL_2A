package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractExpr obj;
    final private AbstractIdentifier meth;
    final private ListExpr param;

    public MethodCall(AbstractExpr obj, AbstractIdentifier meth, ListExpr param) {
        Validate.notNull(obj);
        Validate.notNull(meth);
        Validate.notNull(param);
        this.obj = obj;
        this.meth = meth;
        this.param = param;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (obj != null) {
            obj.decompile(s);
            s.print(".");
        }
        meth.decompile(s);
        s.print("(");
        param.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (obj != null) {
            obj.prettyPrint(s, prefix, false);
        }
        meth.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (obj != null) {
            obj.iter(f);
        }
        meth.iter(f);
        param.iter(f);
    }
}
