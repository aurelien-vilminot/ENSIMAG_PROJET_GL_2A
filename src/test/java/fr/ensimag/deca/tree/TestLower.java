package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLower {
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
        Lower lower = new Lower(intLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(lower.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Lower lower = new Lower(intLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(lower.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Lower lower = new Lower(floatLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(lower.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        Lower lower = new Lower(floatLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(lower.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        Lower lower;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        lower = new Lower(stringLiteralLeft, stringLiteralRight);
        // Check error assertion
        Lower finalGreater = lower;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        lower = new Lower(booleanLiteralLeft,  booleanLiteralRight);
        // Check error assertion
        Lower finalGreater1 = lower;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        lower = new Lower(intLiteralLeft, stringLiteralRight);
        // Check error assertion
        Lower finalGreater2 = lower;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater2.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        lower = new Lower(booleanLiteralLeft,  intLiteralRight);
        // Check error assertion
        Lower finalGreater3 = lower;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater3.verifyExpr(compiler, null, null);
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
