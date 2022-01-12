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

    // @BeforeEach
    void setup() {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
    }

    // @Test
    public void testVerifyExpr() throws DoubleDefException, ContextualError {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));

        // ident to be a declared variable
        localEnv.declare(
            compiler.getSymbolTable().create("ident"),
            new VariableDefinition(INT, Location.BUILTIN)
        );

        assertEquals(INT, ident.verifyExpr(compiler, localEnv, null));
    }

    // @Test
    public void testVerifyExprUndeclaredError() throws DoubleDefException {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));

        // ident to be an undeclared variable

        Exception exception = assertThrows(ContextualError.class, () -> {
            ident.verifyExpr(compiler, localEnv, null);
        });

        String expectedMessage = "Undeclared identifier";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyType() throws ContextualError {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("int"));
        Type intType = compiler.getEnvironmentTypes().get(
                compiler.getSymbolTable().create("int")
            ).getType();
        assertEquals(intType,ident.verifyType(compiler));
    }

    @Test
    public void testVerifyTypeUndefined() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("typo"));
        Exception exception = assertThrows(ContextualError.class, () -> {
            ident.verifyType(compiler);
        });

        String expectedMessage = "Undefined type identifier: typo";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
