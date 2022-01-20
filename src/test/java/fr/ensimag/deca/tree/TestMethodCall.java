package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class TestMethodCall {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    MethodCall methodCall;
    AbstractExpr obj;
    AbstractIdentifier meth;
    ListExpr param;
    AbstractIdentifier VOID, INT;
    @Mock AbstractExpr classInstance;

    @BeforeEach
    void setup() throws EnvironmentTypes.DoubleDefException, ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null,null);
        localEnv = new EnvironmentExp(null);
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        INT = new Identifier(compiler.getSymbolTable().create("int"));

        // Define a class
        SymbolTable.Symbol classObjectSymbol = compiler.getSymbolTable().create("Object");
        SymbolTable.Symbol classSymbol = compiler.getSymbolTable().create("Pangolin");
        ClassType pangolinType = new ClassType(classSymbol, null, null);
        ClassDefinition classDefinition = new ClassDefinition(pangolinType,null,null);
        compiler.getEnvironmentTypes().declare(classSymbol, classDefinition);

        // Configure method call
        meth = new Identifier(compiler.getSymbolTable().create("f"));
        param = new ListExpr();
        param.add(new IntLiteral(42)); // arg1

        // Declare Method ( f(int arg1); )
        ListDeclParam listDeclParam = new ListDeclParam();
        Identifier arg1 = new Identifier(compiler.getSymbolTable().create("arg1"));
        listDeclParam.add(new DeclParam(INT,arg1));
        DeclMethod declMethod = new DeclMethod(VOID, meth, listDeclParam, new MethodBody(new ListDeclVar(), new ListInst()));
        declMethod.verifyDeclMethod(compiler, classObjectSymbol, classSymbol);

        // Create an instance
        when(classInstance.verifyExpr(compiler,localEnv, null)).thenReturn(pangolinType);
    }

    @Test
    public void testVerifyExprObjNotClass() {
        obj = new IntLiteral(0);
        methodCall = new MethodCall(obj, meth, param);
        Exception exception = assertThrows(ContextualError.class, () -> methodCall.verifyExpr(compiler, localEnv, null));
        assertEquals("This identifier is not a class: " + obj.decompile(), exception.getMessage());
    }

    @Test
    public void testVerifyExpr() throws ContextualError{
        obj = classInstance;
        methodCall = new MethodCall(obj, meth, param);
        assertTrue(methodCall.verifyExpr(compiler, localEnv, null).isVoid());
    }
}
