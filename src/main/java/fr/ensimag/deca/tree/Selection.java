package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        int index = ident.getFieldDefinition().getIndex();
        expr.codeGenExpr(compiler, n);
        // Rn contient l'adresse dans le tas
        compiler.addInstruction(new LOAD(new RegisterOffset(index, Register.getR(n)), Register.getR(n)));
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch, int n) {
        codeGenExpr(compiler, n);
        compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
        if (bool) {
            compiler.addInstruction(new BNE(branch));
        } else {
            compiler.addInstruction(new BEQ(branch));
        }
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
