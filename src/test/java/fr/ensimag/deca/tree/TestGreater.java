package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGreater {
    private static DecacCompiler compiler;
    private static BooleanLiteral booleanLiteralLeft;
    private static BooleanLiteral booleanLiteralRight;
    private static FloatLiteral floatLiteralRight;
    private static FloatLiteral floatLiteralLeft;
    private static IntLiteral intLiteralRight;
    private static IntLiteral intLiteralLeft;
    private static StringLiteral stringLiteralRight;
    private static StringLiteral stringLiteralLeft;

    @BeforeAll
    static void setup() {
        compiler = new DecacCompiler(null, null);
        booleanLiteralRight = new BooleanLiteral(true);
        booleanLiteralLeft = new BooleanLiteral(false);
        floatLiteralRight = new FloatLiteral(2);
        floatLiteralLeft = new FloatLiteral(5);
        intLiteralRight = new IntLiteral(1);
        intLiteralLeft = new IntLiteral(2);
        stringLiteralRight = new StringLiteral("foo");
        stringLiteralLeft = new StringLiteral("foo");
    }

    @Test
    public void testIntInt() throws ContextualError {
        Greater greater = new Greater(intLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Greater greater = new Greater(intLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Greater greater = new Greater(floatLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        Greater greater = new Greater(floatLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(greater.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        Greater greater;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        greater = new Greater(stringLiteralLeft, stringLiteralRight);
        // Check error assertion
        Greater finalGreater = greater;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        greater = new Greater(booleanLiteralLeft,  booleanLiteralRight);
        // Check error assertion
        Greater finalGreater1 = greater;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        greater = new Greater(intLiteralLeft, stringLiteralRight);
        // Check error assertion
        Greater finalGreater2 = greater;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater2.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        greater = new Greater(booleanLiteralLeft,  intLiteralRight);
        // Check error assertion
        Greater finalGreater3 = greater;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater3.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
