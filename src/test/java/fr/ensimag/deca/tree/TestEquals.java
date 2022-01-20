package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestEquals {
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
        Equals equals = new Equals(intLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Equals equals = new Equals(intLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Equals equals = new Equals(floatLiteralLeft, intLiteralRight);
        // Check type result of the comparison
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        Equals equals = new Equals(floatLiteralLeft, floatLiteralRight);
        // Check type result of the comparison
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testBoolBool() throws ContextualError {
        Equals equals = new Equals(booleanLiteralLeft, booleanLiteralRight);
        // Check type result of the comparison
        assertTrue(equals.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        Equals equals;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        equals = new Equals(stringLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals = equals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only allowed with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        equals = new Equals(intLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals1 = equals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals1.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only allowed with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        equals = new Equals(booleanLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals2 = equals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals2.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only allowed with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        equals = new Equals(floatLiteralLeft, stringLiteralRight);
        // Check error assertion
        Equals finalEquals3 = equals;
        exception = assertThrows(ContextualError.class, () -> {
            finalEquals3.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Equals or not equals comparison is only allowed with int, float, class, null or boolean types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
