package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGreaterOrEqual {
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
        GreaterOrEqual greaterOrEqual = new GreaterOrEqual(intLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(greaterOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        GreaterOrEqual greaterOrEqual = new GreaterOrEqual(intLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(greaterOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        GreaterOrEqual greaterOrEqual = new GreaterOrEqual(floatLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(greaterOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        GreaterOrEqual greaterOrEqual = new GreaterOrEqual(floatLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(greaterOrEqual.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        GreaterOrEqual greaterOrEqual;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        greaterOrEqual = new GreaterOrEqual(stringLiteralLeft, stringLiteralRight);
        // Check error assertion
        GreaterOrEqual finalGreater = greaterOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        greaterOrEqual = new GreaterOrEqual(booleanLiteralLeft,  booleanLiteralRight);
        // Check error assertion
        GreaterOrEqual finalGreater1 = greaterOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        greaterOrEqual = new GreaterOrEqual(intLiteralLeft, stringLiteralRight);
        // Check error assertion
        GreaterOrEqual finalGreater2 = greaterOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater2.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        greaterOrEqual = new GreaterOrEqual(booleanLiteralLeft,  intLiteralRight);
        // Check error assertion
        GreaterOrEqual finalGreater3 = greaterOrEqual;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater3.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "This comparison works only with int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
