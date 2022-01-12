package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnaryMinus {
    private static DecacCompiler compiler;
    private static BooleanLiteral booleanLiteral;
    private static FloatLiteral floatLiteral;
    private static IntLiteral intLiteral;
    private static StringLiteral stringLiteral;

    @BeforeAll
    static void setup() {
        compiler = new DecacCompiler(null, null);
        booleanLiteral = new BooleanLiteral(true);
        floatLiteral = new FloatLiteral(2);
        intLiteral = new IntLiteral(1);
        stringLiteral = new StringLiteral("foo");
    }

    @Test
    public void testInt() throws ContextualError {
        UnaryMinus unaryMinus = new UnaryMinus(intLiteral);
        // Check type result of the comparison
        assertTrue(unaryMinus.verifyExpr(compiler, null, null).isInt());
    }

    @Test
    public void testFloat() throws ContextualError {
        UnaryMinus unaryMinus = new UnaryMinus(floatLiteral);
        // Check type result of the comparison
        assertTrue(unaryMinus.verifyExpr(compiler, null, null).isFloat());
    }

    @Test
    public void testIncompatibleTypes() {
        UnaryMinus unaryMinus;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        unaryMinus = new UnaryMinus(stringLiteral);
        // Check error assertion
        UnaryMinus finalGreater = unaryMinus;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "Minus operation is only allowed for int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        unaryMinus = new UnaryMinus(booleanLiteral);
        // Check error assertion
        UnaryMinus finalGreater1 = unaryMinus;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "Minus operation is only allowed for int or float types";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
