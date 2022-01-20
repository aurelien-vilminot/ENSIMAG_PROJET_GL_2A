package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestConvFloat {
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
        ConvFloat convFloat = new ConvFloat(intLiteral);
        // Check type result of the comparison
        assertTrue(convFloat.verifyExpr(compiler, null, null).isFloat());
    }

    @Test
    public void testIncompatibleTypes() {
        ConvFloat convFloat;
        Exception exception;
        String expectedMessage;
        String actualMessage;

        convFloat = new ConvFloat(stringLiteral);
        // Check error assertion
        ConvFloat finalGreater = convFloat;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Implicit cast works only with int to float";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        convFloat = new ConvFloat(booleanLiteral);
        // Check error assertion
        ConvFloat finalGreater1 = convFloat;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater1.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Implicit cast works only with int to float";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        convFloat = new ConvFloat(floatLiteral);
        // Check error assertion
        ConvFloat finalGreater2 = convFloat;
        exception = assertThrows(ContextualError.class, () -> {
            finalGreater2.verifyExpr(compiler, null, null);
        });
        expectedMessage = "Implicit cast works only with int to float";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
