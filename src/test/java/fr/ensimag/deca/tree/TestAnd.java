package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAnd {
    private DecacCompiler compiler;

    @BeforeEach
    public void setup() {
        this.compiler = new DecacCompiler(null, null);
    }

    @Test
    public void testBoolBool() throws ContextualError {
        BooleanLiteral booleanLiteralLeft = new BooleanLiteral(true);
        BooleanLiteral booleanLiteralRight = new BooleanLiteral(false);
        And and = new And(booleanLiteralLeft, booleanLiteralRight);
        // Check type result of the boolean operation
        assertTrue(and.verifyExpr(this.compiler, null, null).isInt());
    }

    @Test
    public void testIncompatibleTypes() {
        FloatLiteral floatLiteralLeft = new FloatLiteral(2.0F);
        FloatLiteral floatLiteralRight = new FloatLiteral(2.0F);
        And and = new And(floatLiteralLeft, floatLiteralRight);

        // Check error assertion
        Exception exception = assertThrows(ContextualError.class, () -> {
            and.verifyExpr(this.compiler, null, null).isInt();
        });
        String expectedMessage = "Boolean operation is only allowed for boolean type";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
