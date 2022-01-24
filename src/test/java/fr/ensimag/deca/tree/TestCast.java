package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCast {
    private static DecacCompiler compiler;
    private static BooleanLiteral booleanLiteral;
    private static FloatLiteral floatLiteral;
    private static IntLiteral intLiteral;
    private static AbstractIdentifier identInt;
    private static AbstractIdentifier identFloat;
    private static AbstractIdentifier identBoolean;

    @BeforeAll
    static void setup() throws EnvironmentTypes.DoubleDefException {
        compiler = new DecacCompiler(null, null);
        booleanLiteral = new BooleanLiteral(true);
        floatLiteral = new FloatLiteral(2);
        intLiteral = new IntLiteral(1);
        identInt = new Identifier(compiler.getSymbolTable().create("int"));
        identFloat = new Identifier(compiler.getSymbolTable().create("float"));
        identBoolean = new Identifier(compiler.getSymbolTable().create("boolean"));
    }

    @Test
    public void testSame() throws ContextualError {
        Cast cast = new Cast(identInt, intLiteral);
        assertTrue(cast.verifyExpr(compiler, null, null).isInt());
        cast = new Cast(identFloat, floatLiteral);
        assertTrue(cast.verifyExpr(compiler, null, null).isFloat());
        cast = new Cast(identBoolean, booleanLiteral);
        assertTrue(cast.verifyExpr(compiler, null, null).sameType(identBoolean.getType()));
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Exception exception;
        Cast cast = new Cast(identInt, floatLiteral);
        assertTrue(cast.verifyExpr(compiler, null, null).isInt());
        cast = new Cast(identFloat, intLiteral);
        assertTrue(cast.verifyExpr(compiler, null, null).isFloat());
        cast = new Cast(identBoolean, intLiteral);
        Cast finalCast = cast;
        exception = assertThrows(ContextualError.class, () -> {
           finalCast.verifyExpr(compiler, null, null);
        });
        assertEquals("The origin type (int) cannot be cast into the destination type (boolean)", exception.getMessage());
    }
}
