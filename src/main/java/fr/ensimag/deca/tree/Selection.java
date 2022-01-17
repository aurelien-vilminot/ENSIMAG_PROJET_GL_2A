package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class Selection extends AbstractLValue {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractExpr expr;
    final private AbstractIdentifier ident;

    public Selection(AbstractExpr expr, AbstractIdentifier ident) {
        Validate.notNull(expr);
        Validate.notNull(ident);
        this.expr = expr;
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Selection: start");

        Type classType = this.expr.verifyExpr(compiler, localEnv, currentClass);
        Type identType = this.ident.verifyExpr(compiler, ((ClassDefinition)compiler.getEnvironmentTypes().get(classType.getName())).getMembers(), currentClass);

        TypeDefinition typeDefinition = compiler.getEnvironmentTypes().get(classType.getName());
        if (typeDefinition == null || !typeDefinition.isClass()) {
            throw new ContextualError("Undefined class", this.getLocation());
        }

        if (this.ident.getFieldDefinition().getVisibility() == Visibility.PUBLIC) {
            // Case PUBLIC
            this.setType(identType);
            LOG.debug("verify Selection: end");
            return identType;
        } else {
            // Case PROTECTED
            boolean isSubClass = compiler.getEnvironmentTypes().subTypes(classType, currentClass.getType());
            boolean isSubClassField = compiler.getEnvironmentTypes().subTypes(classType, this.ident.getFieldDefinition().getContainingClass().getType());

            if (isSubClass && isSubClassField) {
                this.setType(identType);
                LOG.debug("verify Selection: end");
                return identType;
            }
        }

        throw new ContextualError("A class is not a subtype of an other class", this.getLocation());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        ident.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        ident.iter(f);
    }
}
