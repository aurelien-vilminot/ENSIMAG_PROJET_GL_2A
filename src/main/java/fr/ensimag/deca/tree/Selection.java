package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Selection extends AbstractLValue {

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
        Type classType = this.expr.verifyExpr(compiler, localEnv, currentClass);

        TypeDefinition typeDefinition = compiler.getEnvironmentTypes().get(classType.getName());
        if (typeDefinition == null || !typeDefinition.isClass()) {
            throw new ContextualError("Undefined class", this.getLocation());
        }

        Type identifierType = null;

        if (this.ident.getVisibility() == Visibility.PUBLIC) {
            // Case PUBLIC
            identifierType = this.ident.verifyType(compiler);
        } else {
            // Case PROTECTED
            boolean isSubClass = compiler.getEnvironmentTypes().subTypes(classType, currentClass.getType());
            boolean isSubClassField = compiler.getEnvironmentTypes().subTypes(classType, this.ident.getFieldDefinition().getContainingClass().getType());

            if (isSubClass && isSubClassField) {
                identifierType = this.ident.verifyType(compiler);
            }
        }

        if (identifierType == null) {
            throw new ContextualError("A class is not a subtype of an other class", this.getLocation());
        }

        return identifierType;
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
