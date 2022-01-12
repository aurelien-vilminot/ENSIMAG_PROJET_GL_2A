package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotEquals {
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
        NotEquals notEquals = new NotEquals(intLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(notEquals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        NotEquals notEquals = new NotEquals(intLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(notEquals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        NotEquals notEquals = new NotEquals(floatLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(notEquals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        NotEquals notEquals = new NotEquals(floatLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(notEquals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testBoolBool() throws ContextualError {
        NotEquals notEquals = new NotEquals(booleanLiteralLeft, booleanLiteralRight);
        // Check type result of the comparison
        assertTrue(notEquals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        Equals notEquals;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        notEquals = new Equals(stringLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals = notEquals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        notEquals = new Equals(intLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals1 = notEquals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals1.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        notEquals = new Equals(booleanLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals2 = notEquals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals2.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        notEquals = new Equals(floatLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals3 = notEquals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals3.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
