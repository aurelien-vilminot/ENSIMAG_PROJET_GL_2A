package fr.ensimag.deca.tree;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

import static org.junit.jupiter.api.Assertions.*;

public class TestIdentifier {
    @Test
    public void testVerifyExpr() throws DoubleDefException, ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        Identifier s1 = new Identifier(compiler.getSymbolTable().create("s1"));

        // s1 to be a declared variable
        EnvironmentExp localEnv = new EnvironmentExp(null);
        Type myType = new IntType(compiler.getSymbolTable().create("int"));
        localEnv.declare(
            compiler.getSymbolTable().create("s1"),
            new VariableDefinition(myType, Location.BUILTIN)
        );

        assertEquals(myType, s1.verifyExpr(compiler, localEnv, null));
    }

    @Test
    public void testVerifyExprUndeclaredError() throws DoubleDefException {
        DecacCompiler compiler = new DecacCompiler(null, null);
        Identifier s2 = new Identifier(compiler.getSymbolTable().create("s2"));

         // s2 to be an undeclared variable
         EnvironmentExp localEnv = new EnvironmentExp(null);
         Type myType = new IntType(compiler.getSymbolTable().create("int"));
         localEnv.declare(
             compiler.getSymbolTable().create("s1"),
             new VariableDefinition(myType, Location.BUILTIN)
         );

        Exception exception = assertThrows(ContextualError.class, () -> {
            s2.verifyExpr(compiler, localEnv, null);
        });

        String expectedMessage = "Undeclared identifier";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
