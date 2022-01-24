package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class TestArrayAccess {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    ArrayAccess arrayAccess;
    AbstractExpr index;
    @Mock AbstractExpr arrayInstance, intInstance;

    @BeforeEach
    void setup() throws ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);

        // Instance Mocks Config
        Type INT_ARRAY = new VectorIntType(compiler.getSymbolTable().create("int[]"));
        Type INT = new IntType(compiler.getSymbolTable().create("int"));
        when(arrayInstance.verifyExpr(compiler, localEnv, null)).thenReturn(INT_ARRAY);
        when(intInstance.verifyExpr(compiler, localEnv, null)).thenReturn(INT);
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        index = new IntLiteral(0);
        arrayAccess = new ArrayAccess(arrayInstance, index);
        assertTrue(arrayAccess.verifyExpr(compiler, localEnv, null).isInt());
    }

    @Test
    public void testVerifyExprNonIntIndexError() {
        index = new StringLiteral("zero");
        arrayAccess = new ArrayAccess(arrayInstance, index);
        Exception exception = assertThrows(ContextualError.class, () -> arrayAccess.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "The index for array access must be an integer";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyExprVariableNotArrayError() {
        index = new IntLiteral(0);
        arrayAccess = new ArrayAccess(intInstance, index);
        Exception exception = assertThrows(ContextualError.class, () -> arrayAccess.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "Identifier for array access must be a 1D or 2D array of float or int";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
