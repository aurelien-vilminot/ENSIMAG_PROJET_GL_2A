package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Draft_TestMethodCall {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    MethodCall methodCall;
    AbstractExpr obj;
    AbstractIdentifier meth;
    ListExpr param;

    @BeforeEach
    void setup() {
        compiler = new DecacCompiler(null,null);
        localEnv = new EnvironmentExp(null);
        meth = new Identifier(compiler.getSymbolTable().create("f"));
        param = new ListExpr();
    }

    @Test
    public void testVerifyExprObjNotClass() {
        obj = new IntLiteral(0);
        methodCall = new MethodCall(obj, meth, param);
        Exception exception = assertThrows(ContextualError.class, () -> methodCall.verifyExpr(compiler, localEnv, null));
        assertEquals("This identifier is not a class: " + obj.decompile(), exception.getMessage());
    }

    //@Test
    public void testVerifyExpr() {
        // obj = new Class();
    }
}
