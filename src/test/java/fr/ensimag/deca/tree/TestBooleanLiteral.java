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
}
