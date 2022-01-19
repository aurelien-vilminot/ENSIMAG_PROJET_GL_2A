package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
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
        Type typeClass = this.obj.verifyExpr(compiler, localEnv, currentClass);

        if (!compiler.getEnvironmentTypes().get(typeClass.getName()).isClass()) {
            // If object is not a class type
            throw new ContextualError("Undefined class", this.getLocation());
        }
        this.obj.setType(typeClass);

        // Get environment exp of class object
        EnvironmentExp environmentExp2 = ((ClassDefinition) compiler.getEnvironmentTypes().get(typeClass.getName())).getMembers();
        MethodDefinition methodDefinition = this.meth.verifyMethod(compiler, environmentExp2);

        int i = 0;
        for (AbstractExpr param: this.param.getList()) {
            Type expectedType = methodDefinition.getSignature().paramNumber(i++);
            param.verifyRValue(compiler, environmentExp2, (ClassDefinition) compiler.getEnvironmentTypes().get(typeClass.getName()), expectedType);
        }

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
        compiler.addInstruction(new ADDSP(new ImmediateInteger(param.size() + 1)));

        // Load implicit parameter
        obj.codeGenExpr(compiler, 2);
        compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(0, Register.SP)));

        // Load parameters
        int index = -1;
        for (AbstractExpr expr : param.getList()) {
            expr.codeGenExpr(compiler, 2);
            compiler.addInstruction(new STORE(Register.getR(2), new RegisterOffset(index, Register.SP)));
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.getR(2)));

        // Get method table address
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(2)), Register.getR(2)));
        int methodIndex = meth.getMethodDefinition().getIndex();
        compiler.addInstruction(new BSR(new RegisterOffset(methodIndex, Register.getR(2))));
        compiler.addInstruction(new SUBSP(param.size() + 1));
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
