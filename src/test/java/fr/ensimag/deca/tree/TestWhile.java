package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class TestWhile {
    private DecacCompiler compiler;
    private EnvironmentExp localEnv;
    private While w;
    private AbstractExpr condition;
    private ListInst body;

    @Mock
    AbstractExpr conditionNonBool;

    @Mock
    BooleanType fakeBoolType;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        condition = new BooleanLiteral(true);
        body = new ListInst();
        w = new While(condition, body);

        when(conditionNonBool.getType()).thenReturn(fakeBoolType);
        when(fakeBoolType.isBoolean()).thenReturn(false);
    }

    @Test
    public void testGetCondition() {
        assertEquals(condition, w.getCondition());
    }

    @Test
    public void testGetBody() {
        assertEquals(body, w.getBody());
    }

    @Test
    public void testVerifyInst() {
        w = new While(conditionNonBool, body);

        Exception exception = assertThrows(ContextualError.class, () -> {
            w.verifyInst(compiler, localEnv, null, fakeBoolType);
        });

        String expectedMessage = "The condition must be only boolean type";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
