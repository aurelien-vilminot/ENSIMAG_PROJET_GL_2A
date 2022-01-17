package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;

public class TestAbstractExpr {
    private DecacCompiler compiler;
    private AbstractExpr expr;

    @BeforeEach
    void setup() {
        compiler = new DecacCompiler(null, null);
    }

    @Test
    public void testCheckDecorationNoTypeError() {
        // this BooleanLiteral has not been decorated
        // with contextual syntax
        expr = new BooleanLiteral(true);

        Exception exception = assertThrows(DecacInternalError.class, () -> {
            expr.checkDecoration();
        });

        String expectedMessage = "Expression " + expr.decompile() + " has no Type decoration";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyConditionNotBoolean() {
        // Should only verify condition on a boolean expression
        expr = new StringLiteral("str");

        Exception exception = assertThrows(ContextualError.class, () -> {
            expr.verifyCondition(compiler, null, null);
        });

        String expectedMessage = "Expression type must be boolean";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testIsImplicit() {
        expr = new BooleanLiteral(true);
        assertEquals(false, expr.isImplicit());
    }
    
}
