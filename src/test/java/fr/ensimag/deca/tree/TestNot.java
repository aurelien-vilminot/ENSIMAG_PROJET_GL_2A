package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNot {
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
    public void testBoolean() throws ContextualError {
        Not not = new Not(booleanLiteral);
        // Check type result of the comparison
        assertTrue(not.verifyExpr(compiler, null, null).isBoolean());
    }

    @Test
    public void testIncompatibleTypes() {
        Not not;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        not = new Not(stringLiteral);
        // Check error assertion
        Not finalGreater = not;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "Not operation is only allowed for boolean type";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        not = new Not(intLiteral);
        // Check error assertion
        Not finalGreater1 = not;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "Not operation is only allowed for boolean type";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        not = new Not(floatLiteral);
        // Check error assertion
        Not finalGreater2 = not;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater2.verifyExpr(compiler, null, null).isInt();
        });
        expectedMessage = "Not operation is only allowed for boolean type";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
