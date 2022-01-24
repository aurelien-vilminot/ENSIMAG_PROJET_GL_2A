package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class TestReturn {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    Return returnInst;
    @Mock AbstractExpr expr;
    @Mock Type voidType;
    @Mock Type boolType;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        returnInst = new Return(expr);
        when(voidType.isVoid()).thenReturn(true);
        when(boolType.isVoid()).thenReturn(false);
        when(boolType.isBoolean()).thenReturn(true);
    }

    @Test
    public void testGetExpr() {
        AbstractExpr boolExpr = new BooleanLiteral(true);
        returnInst = new Return(boolExpr);
        assertEquals(boolExpr, returnInst.getExpr());
    }

    @Test
    public void testVerifyInstReturnVoidError() {
        Exception exception = assertThrows(ContextualError.class, () -> returnInst.verifyInst(compiler, localEnv, null, voidType));

        String expectedMessage = "Method return type is void";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyInst() throws ContextualError {
        returnInst.verifyInst(compiler, localEnv, null, boolType);
    }
}
