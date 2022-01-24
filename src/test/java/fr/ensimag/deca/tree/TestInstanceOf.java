package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;


public class TestInstanceOf {
    private DecacCompiler compiler;
    private EnvironmentExp localEnv;

    @Mock
    AbstractExpr classExpr;
    @Mock
    AbstractIdentifier classId;
    @Mock
    Type classType;

    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.openMocks(this);

        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);

        when(classType.isClass()).thenReturn(true);
        when(classType.isClassOrNull()).thenReturn(true);
        when(classExpr.verifyExpr(compiler, localEnv, null)).thenReturn(classType);
        when(classId.verifyType(compiler)).thenReturn(classType);
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        InstanceOf i = new InstanceOf(classExpr, classId);
        i.verifyExpr(compiler, localEnv, null);
        assertTrue(i.getType().isBoolean());
    }
}
