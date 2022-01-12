package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;


public class TestIdentifier {

    final Type INT = new IntType(null);

    private DecacCompiler compiler;
    private EnvironmentExp localEnv;

    @BeforeEach
    void setup() {
        DecacCompiler compiler = new DecacCompiler(null, null);
        EnvironmentExp localEnv = new EnvironmentExp(null);
    }

    @Test
    public void testVerifyExpr() throws DoubleDefException, ContextualError {
        Identifier s1 = new Identifier(compiler.getSymbolTable().create("s1"));

        // s1 to be a declared variable
        localEnv.declare(
            compiler.getSymbolTable().create("s1"),
            new VariableDefinition(INT, Location.BUILTIN)
        );

        assertEquals(INT, s1.verifyExpr(compiler, localEnv, null));
    }

    @Test
    public void testVerifyExprUndeclaredError() throws DoubleDefException {
        Identifier s2 = new Identifier(compiler.getSymbolTable().create("s2"));

        // s2 to be an undeclared variable

        Exception exception = assertThrows(ContextualError.class, () -> {
            s2.verifyExpr(compiler, localEnv, null);
        });

        String expectedMessage = "Undeclared identifier";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
