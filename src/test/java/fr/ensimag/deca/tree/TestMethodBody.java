package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestMethodBody {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    MethodBody methodBody;

    @Mock Type returnType;
    @Mock ClassDefinition classDefinition;

    @BeforeEach
    void setup() throws ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        methodBody = new MethodBody(new ListDeclVar(), new ListInst());
    }

    @Test
    public void testVerifyMethodBody() throws ContextualError {
        methodBody.verifyMethodBody(compiler, localEnv, classDefinition, returnType);
    }

}

