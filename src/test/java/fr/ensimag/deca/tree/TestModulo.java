package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestModulo {
    private DecacCompiler compiler;

    @BeforeEach
    public void setup() {
        this.compiler = new DecacCompiler(null, null);
    }

    @Test
    public void testIntInt() throws ContextualError {
        IntLiteral intLiteralLeft = new IntLiteral(10);
        IntLiteral intLiteralRight = new IntLiteral(10);
        Modulo modulo = new Modulo(intLiteralLeft, intLiteralRight);
        // Check type result of the addition
        assertTrue(modulo.verifyExpr(this.compiler, null, null).isInt());
    }

    @Test
    public void testIncompatibleTypes() {
        FloatLiteral floatLiteralLeft = new FloatLiteral(2.0F);
        FloatLiteral floatLiteralRight = new FloatLiteral(2.0F);
        Modulo modulo = new Modulo(floatLiteralLeft, floatLiteralRight);

        // Check error assertion
        Exception exception = assertThrows(ContextualError.class, () -> {
            modulo.verifyExpr(this.compiler, null, null).isInt();
        });
        String expectedMessage = "Modulo operation is only allowed for int type";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
}
