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
}
