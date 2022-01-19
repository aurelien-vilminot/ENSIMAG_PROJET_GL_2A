package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;

public class TestMethodAsmBody {
    @Test
    public void testVerifyMethodBody() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        MethodAsmBody mab = new MethodAsmBody(new StringLiteral("HLT"));
        mab.verifyMethodBody(compiler, null, null, null);
    }
}
