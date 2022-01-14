package fr.ensimag.deca.tree;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestFloatLiteral {
    @Test
    public void testVerifyFloatExpression() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        FloatLiteral literal = new FloatLiteral(0);
        literal.verifyExpr(compiler, null, null);
        assertTrue(literal.getType().isFloat());
    }

    @Test
    public void testConstructorErrorInfinite() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FloatLiteral(Float.POSITIVE_INFINITY);
        });
        String expectedMessage = "literal values cannot be infinite";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void testConstructorErrorNaN() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new FloatLiteral(Float.NaN);
        });
        String expectedMessage = "literal values cannot be NaN";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
