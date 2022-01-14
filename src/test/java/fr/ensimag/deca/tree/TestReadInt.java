package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

public class TestReadInt {
    @Test
    public void testVerifyExpr() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        ReadInt ri = new ReadInt();
        ri.verifyExpr(compiler, null, null);
        assertTrue(ri.getType().isInt());
    }
}
