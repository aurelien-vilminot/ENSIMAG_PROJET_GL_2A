package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
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
        LOG.debug("verify MethodCall: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Local environment object should not be null");

        Type typeClass = this.obj.verifyExpr(compiler, localEnv, currentClass);

        if (compiler.getEnvironmentTypes().get(typeClass.getName()) == null ||
                !compiler.getEnvironmentTypes().get(typeClass.getName()).isClass()) {
            // If object is not a class type
            throw new ContextualError("This identifier is not a class: " + this.obj.decompile(), this.getLocation());
        }
        this.obj.setType(typeClass);

        // Get environment exp of class object
        EnvironmentExp environmentExp2 = ((ClassDefinition) compiler.getEnvironmentTypes().get(typeClass.getName())).getMembers();
        MethodDefinition methodDefinition = this.meth.verifyMethod(compiler, environmentExp2);

        if (this.param.getList().size() != methodDefinition.getSignature().size()) {
            throw new ContextualError("Number of parameters doesn't match method signature", this.getLocation());
        }

        int i = 0;
        for (AbstractExpr param: this.param.getList()) {
            Type expectedType = methodDefinition.getSignature().paramNumber(i);
            param.verifyRValue(
                    compiler,
                    localEnv,
                    (ClassDefinition) compiler.getEnvironmentTypes().get(typeClass.getName()),
                    expectedType
            );

            if (expectedType.isFloat() && param.getType().isInt()) {
                // Implicit float conversion
                AbstractExpr convFloat = new ConvFloat(param);
                convFloat.setType(expectedType);
                this.param.set(i, convFloat);
            }
            i++;
        }

        this.setType(methodDefinition.getType());

        LOG.debug("verify MethodCall: end");
        return methodDefinition.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (!obj.isImplicit()) {
            obj.decompile(s);
            s.print(".");
        }
        meth.decompile(s);
        s.print("(");
        param.decompile(s);
        s.print(")");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        boolean pop = false;
        if (compiler.getCurrentRegister() >= 2) {
            compiler.incTempStackCurrent(1);
            compiler.addInstruction(new PUSH(Register.getR(2)));
            pop = true;
        }

        compiler.addInstruction(new ADDSP(new ImmediateInteger(param.size() + 1)));

        // Load implicit parameter
        obj.codeGenExpr(compiler, 2);
        compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(0, Register.SP)));

        // Load parameters
        int index = -1;
        for (AbstractExpr expr : param.getList()) {
            expr.codeGenExpr(compiler, 2);
            compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(index--, Register.SP)));
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.getR(2)));
        compiler.addDereference(2);

        // Get method table address
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(2)), Register.getR(2)));
        int methodIndex = meth.getMethodDefinition().getIndex();
        compiler.addInstruction(new BSR(new RegisterOffset(methodIndex, Register.getR(2))));
        compiler.addInstruction(new SUBSP(param.size() + 1));

        if (pop) {
            compiler.addInstruction(new POP(Register.getR(2)));
            compiler.incTempStackCurrent(-1);
        }
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        codeGenInst(compiler);
        compiler.addInstruction(new LOAD(Register.R0, Register.getR(n)));
    }

    @Override
    protected void codeGenExprBool(DecacCompiler compiler, boolean bool, Label branch, int n) {
        compiler.setAndVerifyCurrentRegister(n);

        // Calculate the selection and load result into Rn
        codeGenExpr(compiler, n);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.getR(n)));
        if (bool) {
            compiler.addInstruction(new BNE(branch));
        } else {
            compiler.addInstruction(new BEQ(branch));
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        meth.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        meth.iter(f);
        param.iter(f);
    }
}
