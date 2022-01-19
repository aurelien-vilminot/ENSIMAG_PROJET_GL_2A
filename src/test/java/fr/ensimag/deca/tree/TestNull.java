package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNull {
    @Test
    public void testVerifyExpr() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        Null n = new Null();
        n.verifyExpr(compiler, null, null);
        assertTrue(n.getType().isNull());
    }
}
