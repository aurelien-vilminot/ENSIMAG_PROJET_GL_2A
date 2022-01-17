package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

public class TestReadFloat {
    @Test
    public void testVerifyExpr() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        ReadFloat rf = new ReadFloat();
        rf.verifyExpr(compiler, null, null);
        assertTrue(rf.getType().isFloat());
    }
}
