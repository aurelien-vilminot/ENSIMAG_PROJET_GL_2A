package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMinus {
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
        Minus minus = new Minus(this.floatLiteralLeft, this.floatLiteralRight);
        // Check type result of the substraction
        assertTrue(minus.verifyExpr(this.compiler, null, null).isFloat());
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Minus minus = new Minus(this.floatLiteralLeft, this.intLiteralRight);
        // Check type result of the substraction
        assertTrue(minus.verifyExpr(this.compiler, null, null).isFloat());
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Minus minus = new Minus(this.intLiteralLeft, this.floatLiteralRight);
        // Check type result of the substraction
        assertTrue(minus.verifyExpr(this.compiler, null, null).isFloat());
    }

    @Test
    public void testIntInt() throws ContextualError {
        Minus minus = new Minus(this.intLiteralLeft, this.intLiteralRight);
        // Check type result of the substraction
        assertTrue(minus.verifyExpr(this.compiler, null, null).isInt());
    }

    @Test
    public void testIncompatibleTypes() {
        StringLiteral stringLiteralLeft = new StringLiteral("foo");
        StringLiteral stringLiteralRight = new StringLiteral("bar");
        Minus minus = new Minus(stringLiteralLeft, stringLiteralRight);

        // Check error assertion
        Exception exception = assertThrows(ContextualError.class, () -> {
            minus.verifyExpr(this.compiler, null, null).isInt();
        });
        String expectedMessage = "Binary operation is only allowed for int or float types";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
