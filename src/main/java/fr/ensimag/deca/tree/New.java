package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class New extends AbstractExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractIdentifier ident;

    public New(AbstractIdentifier ident) {
        Validate.notNull(ident);
        this.ident = ident;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify New: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        Type type = this.ident.verifyType(compiler);
        if (!type.isClass()) {
            throw new ContextualError("Cannot instantiate a class, the type must a class", this.getLocation());
        }

        LOG.debug("verify New: end");
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        ident.decompile(s);
        s.print("()");
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        Validate.isTrue((n <= compiler.getCompilerOptions().getRegisterNumber() - 1));

        // heap allocation
        int size = ident.getClassDefinition().getNumberOfFields() + 1;
        DAddr methodAddr = compiler.getEnvironmentExp().get(ident.getName()).getOperand();
        compiler.addInstruction(new NEW(size, Register.getR(n)));
        compiler.addOverflowError();
        // store method table adress in Rn
        compiler.addInstruction(new LEA(methodAddr, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, Register.getR(n))));
        // save Rn
        compiler.addInstruction(new PUSH(Register.getR(n)));
        // initialize object
        compiler.addInstruction(new BSR(new Label("init." + ident.getName())));
        // remove Rn from stack
        compiler.addInstruction(new POP(Register.getR(n)));
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        ident.iter(f);
    }
}