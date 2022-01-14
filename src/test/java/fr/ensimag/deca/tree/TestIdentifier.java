package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.DecacInternalError;


public class TestIdentifier {

    final Type INT = new IntType(null);

    private DecacCompiler compiler;
    private EnvironmentExp localEnv;

    @BeforeEach
    void setup() {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
    }

    @Test
    public void testVerifyExpr() throws DoubleDefException, ContextualError {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));

        // ident to be a declared variable
        localEnv.declare(
            compiler.getSymbolTable().create("ident"),
            new VariableDefinition(INT, Location.BUILTIN)
        );

        assertEquals(INT, ident.verifyExpr(compiler, localEnv, null));
    }

    @Test
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

    @Test
    public void testGetClassDefinition() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        ClassDefinition rightDef = new ClassDefinition(null, null, null);

        // test cast with right identifier type
        ident.setDefinition(rightDef);
        assertEquals(rightDef, ident.getClassDefinition());
    }

    @Test
    public void testGetClassDefinitionTypeMismatch() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        FieldDefinition wrongDef = new FieldDefinition(null, null, null, null, 0);

        // test cast with wrong identifier type
        ident.setDefinition(wrongDef);
        Exception exception = assertThrows(DecacInternalError.class, () -> {
            ident.getClassDefinition();
        });

        String expectedMessage = "Identifier ident is not a class identifier, you can't call getClassDefinition on it";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetMethodDefinition() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        MethodDefinition rightDef = new MethodDefinition(null, null, null, 0);

        // test cast with right identifier type
        ident.setDefinition(rightDef);
        assertEquals(rightDef, ident.getMethodDefinition());
        
    }

    @Test
    public void testGetMethodDefinitionTypeMismatch() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        FieldDefinition wrongDef = new FieldDefinition(null, null, null, null, 0);

        // test cast with wrong identifier type
        ident.setDefinition(wrongDef);
        Exception exception = assertThrows(DecacInternalError.class, () -> {
            ident.getMethodDefinition();
        });

        String expectedMessage = "Identifier ident is not a method identifier, you can't call getMethodDefinition on it";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetFieldDefinition() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        FieldDefinition rightDef = new FieldDefinition(null, null, null, null, 0);

        // test cast with right identifier type
        ident.setDefinition(rightDef);
        assertEquals(rightDef, ident.getFieldDefinition());
    }

    @Test
    public void testGetFieldDefinitionTypeMismatch() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        MethodDefinition wrongDef = new MethodDefinition(null, null, null, 0);

        // test cast with wrong identifier type
        ident.setDefinition(wrongDef);
        Exception exception = assertThrows(DecacInternalError.class, () -> {
            ident.getFieldDefinition();
        });

        String expectedMessage = "Identifier ident is not a field identifier, you can't call getFieldDefinition on it";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetVariableDefinition() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        VariableDefinition rightDef = new VariableDefinition(null, null);

        // test cast with right identifier type
        ident.setDefinition(rightDef);
        assertEquals(rightDef, ident.getVariableDefinition());
    }

    @Test
    public void testGetVariableDefinitionTypeMismatch() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        MethodDefinition wrongDef = new MethodDefinition(null, null, null, 0);

        // test cast with wrong identifier type
        ident.setDefinition(wrongDef);
        Exception exception = assertThrows(DecacInternalError.class, () -> {
            ident.getVariableDefinition();
        });

        String expectedMessage = "Identifier ident is not a variable identifier, you can't call getVariableDefinition on it";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testGetExpDefinition() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        ExpDefinition rightDef = new VariableDefinition(null, null);

        // test cast with right identifier type
        ident.setDefinition(rightDef);
        assertEquals(rightDef, ident.getExpDefinition());
    }

    @Test
    public void testGetExpDefinitionTypeMismatch() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        TypeDefinition wrongDef = new TypeDefinition(null, null);

        // test cast with wrong identifier type
        ident.setDefinition(wrongDef);
        Exception exception = assertThrows(DecacInternalError.class, () -> {
            ident.getExpDefinition();
        });

        String expectedMessage = "Identifier ident is not a Exp identifier, you can't call getExpDefinition on it";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testCheckDecoration() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        ident.setDefinition(null);

        Exception exception = assertThrows(DecacInternalError.class, () -> {
            ident.checkDecoration();
        });

        String expectedMessage = "Identifier ident has no attached Definition";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testSetDefinition() {
        Identifier ident = new Identifier(compiler.getSymbolTable().create("ident"));
        Definition myDefinition = new TypeDefinition(null, null);
        ident.setDefinition(myDefinition);

        assertEquals(myDefinition, ident.getDefinition());
    }
}
