package fr.ensimag.deca.tree;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestBooleanLiteral {
    @Test
    public void testVerifyExpr() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        BooleanLiteral literal = new BooleanLiteral(true);
        literal.verifyExpr(compiler, null, null);
        assertTrue(literal.getType().isBoolean());
    }

    @Test
    public void testDVal() {
        DecacCompiler compiler = new DecacCompiler(null, null);
        BooleanLiteral T = new BooleanLiteral(true);
        BooleanLiteral F = new BooleanLiteral(false);
        assertEquals("#1",T.dval(compiler).toString());
        assertEquals("#0",F.dval(compiler).toString());
    }

    @Test
    public void testGetValue() {
        BooleanLiteral literal = new BooleanLiteral(true);
        assertTrue(literal.getValue());
    }
}
