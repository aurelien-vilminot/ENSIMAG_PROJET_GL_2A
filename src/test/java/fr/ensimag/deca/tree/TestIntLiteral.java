package fr.ensimag.deca.tree;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestIntLiteral {
    @Test
    public void testVerifyIntExpression() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        IntLiteral literal = new IntLiteral(0);
        literal.verifyExpr(compiler, null, null);
        assertTrue(literal.getType().isInt());
    }
}
