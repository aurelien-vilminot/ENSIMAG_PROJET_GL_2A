package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class Cast extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractIdentifier type;
    final private AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.type = type;
        this.expr = expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify Cast: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        // Get cast type
        Type castType = this.type.verifyType(compiler);
        Type exprType = this.expr.verifyExpr(compiler, localEnv, currentClass);

        boolean isCastCompatible = compiler.getEnvironmentTypes().castCompatible(exprType, castType);
        if (!isCastCompatible) {
            throw new ContextualError(
                    "The origin type (" + exprType + ") cannot be cast into the destination type (" + castType +")"
                    , this.getLocation()
            );
        }

        this.setType(castType);
        LOG.debug("verify Cast: end");
        return castType;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(") (");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        Type castType = type.getType();
        Type exprType = expr.getType();
        if (exprType.sameType(castType)) {
            expr.codeGenExpr(compiler, n);
        } else if (exprType.isInt() && castType.isFloat()) {
            expr.codeGenExpr(compiler, n);
            // Rn <- ConversionFlottant(V[Rn])
            compiler.addInstruction(new FLOAT(Register.getR(n), Register.getR(n)));
        } else if (exprType.isFloat() && castType.isInt()) {
            expr.codeGenExpr(compiler, n);
            // Rn <- ConversionEntier(V[Rn])
            compiler.addInstruction(new INT(Register.getR(n), Register.getR(n)));
        } else {
            // Nothing to do
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
}
