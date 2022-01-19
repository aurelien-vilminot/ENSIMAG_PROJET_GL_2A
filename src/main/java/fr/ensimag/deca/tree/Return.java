package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class Return extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private AbstractExpr expr;

    public AbstractExpr getExpr() {
        return expr;
    }

    public Return(AbstractExpr expr) {
        Validate.notNull(expr);
        this.expr = expr;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        expr.codeGenExpr(compiler, 0);
        compiler.addInstruction(new BRA(compiler.getLabelGenerator().getEndLabel()));
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify Return: start");

        if (returnType.isVoid()) {
            throw new ContextualError("Return type cannot be void", this.getLocation());
        }

        this.expr.verifyRValue(compiler, localEnv, currentClass, returnType);

        LOG.debug("verify Return: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        getExpr().decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, true);
    }

}
