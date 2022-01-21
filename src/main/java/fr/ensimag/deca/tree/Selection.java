package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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

        TypeDefinition typeDefinition = compiler.getEnvironmentTypes().get(classType.getName());
        if (typeDefinition == null || !typeDefinition.isClass()) {
            throw new ContextualError("Can't select field from non-class type : " + this.expr.getType().getName(), this.getLocation());
        }

        Type identType = this.ident.verifyExpr(compiler, ((ClassDefinition)compiler.getEnvironmentTypes().get(classType.getName())).getMembers(), currentClass);

        if (this.ident.getFieldDefinition().getVisibility() == Visibility.PUBLIC) {
            // Case PUBLIC
            this.setType(identType);
            LOG.debug("verify Selection: end");
            return identType;
        } else {
            // Case PROTECTED
            if (currentClass == null) {
                // Cannot access to protected field in the main program
                throw new ContextualError(
                        "The identifier '" + this.ident.getName() + "' is protected and it is impossible to access it in the main program",
                        this.getLocation());
            }
            boolean isSubClass = compiler.getEnvironmentTypes().subTypes(currentClass.getType(), classType);
            boolean isSubClassField = compiler.getEnvironmentTypes().subTypes(classType, this.ident.getFieldDefinition().getContainingClass().getType());

            if (isSubClass && isSubClassField) {
                this.setType(identType);
                LOG.debug("verify Selection: end");
                return identType;
            }
        }

        throw new ContextualError("Impossible to select this identifier : " + this.ident.getName(), this.getLocation());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        ident.decompile(s);
    }

    @Override
    protected void codeGenStore(DecacCompiler compiler, int n) {
        int maxRegister = compiler.setAndVerifyCurrentRegister(n);

        int index = ident.getFieldDefinition().getIndex();
        if (n < maxRegister) {
            // Calculate heap address of the object into Rn+1
            expr.codeGenExpr(compiler, n+1);
            compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(index, Register.getR(n+1))));
        } else {
            compiler.incTempStackCurrent(1);
            compiler.addInstruction(new PUSH(Register.getR(n)), "save");
            // Calculate heap address of the object into Rn
            expr.codeGenExpr(compiler, n);
            compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
            // R0 contains heap address of the object
            compiler.addInstruction(new POP(Register.getR(n)), "restore");
            compiler.incTempStackCurrent(-1);
            // Load R0 into correct field in the object
            compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(index, Register.R0)));
        }
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        int index = ident.getFieldDefinition().getIndex();
        // Calculate heap address of the object into Rn
        expr.codeGenExpr(compiler, n);
        if (expr.getType().isClass()) {
            compiler.addDereference(n);
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(index, Register.getR(n)), Register.getR(n)));
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        // Calculate the selection and load result into Rn
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
