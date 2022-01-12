package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLowerOrEqual {
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
        LowerOrEqual lowerOrEqual = new LowerOrEqual(intLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        LowerOrEqual lowerOrEqual = new LowerOrEqual(intLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        LowerOrEqual lowerOrEqual = new LowerOrEqual(floatLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        LowerOrEqual lowerOrEqual = new LowerOrEqual(floatLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(lowerOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        LowerOrEqual lowerOrEqual;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        lowerOrEqual = new LowerOrEqual(stringLiteralLeft, stringLiteralRight);
        // Check error assertion
        LowerOrEqual finalGreater = lowerOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        lowerOrEqual = new LowerOrEqual(booleanLiteralLeft,  booleanLiteralRight);
        // Check error assertion
        LowerOrEqual finalGreater1 = lowerOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        lowerOrEqual = new LowerOrEqual(intLiteralLeft, stringLiteralRight);
        // Check error assertion
        LowerOrEqual finalGreater2 = lowerOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater2.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        lowerOrEqual = new LowerOrEqual(booleanLiteralLeft,  intLiteralRight);
        // Check error assertion
        LowerOrEqual finalGreater3 = lowerOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater3.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
