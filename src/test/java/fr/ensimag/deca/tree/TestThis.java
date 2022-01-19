package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TestThis {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    This thisRef;

    @Mock ClassDefinition currentClass;
    @Mock ClassType classType;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        thisRef = new This();
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        when(currentClass.getType()).thenReturn(classType);
        when(classType.isClass()).thenReturn(true);
    }

    @Test
    public void testVerifyExprMain() {
        Exception exception = assertThrows(ContextualError.class, () -> {
            thisRef.verifyExpr(compiler, localEnv, null);
        });

        String expectedMessage = "Impossible to use 'this' identifier in the main program";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyExpr() throws ContextualError {
        thisRef.verifyExpr(compiler, localEnv, currentClass);
        assertEquals(classType, thisRef.getType());
    }
}
