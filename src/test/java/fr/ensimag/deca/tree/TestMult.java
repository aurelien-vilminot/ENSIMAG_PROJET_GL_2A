package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMult {
    private DecacCompiler compiler;
    private IntLiteral intLiteralRight;
    private IntLiteral intLiteralLeft;
    private FloatLiteral floatLiteralRight;
    private FloatLiteral floatLiteralLeft;

    @BeforeEach
    public void setup() {
        this.compiler = new DecacCompiler(null, null);
        this.floatLiteralRight = new FloatLiteral(2);
        this.floatLiteralLeft = new FloatLiteral(2);
        this.intLiteralRight = new IntLiteral(10);
        this.intLiteralLeft = new IntLiteral(10);
    }

    @Test
    public void testFloatFloat() throws ContextualError {
        Multiply multiply = new Multiply(this.floatLiteralLeft, this.floatLiteralRight);
        // Check type result of the multiplication
        assertTrue(multiply.verifyExpr(this.compiler, null, null).isFloat());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Multiply multiply = new Multiply(this.floatLiteralLeft, this.intLiteralRight);
        // Check type result of the multiplication
        assertTrue(multiply.verifyExpr(this.compiler, null, null).isFloat());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Multiply multiply = new Multiply(this.intLiteralLeft, this.floatLiteralRight);
        // Check type result of the multiplication
        assertTrue(multiply.verifyExpr(this.compiler, null, null).isFloat());
    }

    @Test
    public void testIntInt() throws ContextualError {
        Multiply multiply = new Multiply(this.intLiteralLeft, this.intLiteralRight);
        // Check type result of the multiplication
        assertTrue(multiply.verifyExpr(this.compiler, null, null).isInt());
    }

    @Test
    public void testIncompatibleTypes() {
        StringLiteral stringLiteralLeft = new StringLiteral("foo");
        StringLiteral stringLiteralRight = new StringLiteral("bar");
        Multiply multiply = new Multiply(stringLiteralLeft, stringLiteralRight);

        // Check error assertion
        Exception exception = assertThrows(ContextualError.class, () -> {
            multiply.verifyExpr(this.compiler, null, null);
        });
        String expectedMessage = "Binary operation is only allowed for int or float types";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
