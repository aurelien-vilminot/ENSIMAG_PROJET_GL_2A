package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestNewArray {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    NewArray newArray;
    AbstractIdentifier INT, FLOAT, VOID;
    ListExpr indexList;

    @BeforeEach
    void setup() {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        INT = new Identifier(compiler.getSymbolTable().create("int"));
        FLOAT = new Identifier(compiler.getSymbolTable().create("float"));
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        indexList = new ListExpr();
    }

    @Test
    public void testVerifyExprVectorInt() throws ContextualError {
        // new int[100];
        indexList.add(new IntLiteral(100));
        newArray = new NewArray(INT, indexList);
        assertTrue(newArray.verifyExpr(compiler, localEnv, null).isVectorInt());
    }

    @Test
    public void testVerifyExprVectorFloat() throws ContextualError {
        // new float[100];
        indexList.add(new IntLiteral(100));
        newArray = new NewArray(FLOAT, indexList);
        assertTrue(newArray.verifyExpr(compiler, localEnv, null).isVectorFloat());
    }

    @Test
    public void testVerifyExprNoBrackets() throws ContextualError {
        // new int; (should not come from parser)
        newArray = new NewArray(INT, indexList);
        assertTrue(newArray.verifyExpr(compiler, localEnv, null).isInt());
    }

    @Test
    public void testVerifyExprPrimitiveTypeError() {
        // new void[100];
        newArray = new NewArray(VOID, indexList);
        Exception exception = assertThrows(ContextualError.class, () -> newArray.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "An array must contain float or int";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyExprNonIntIndexError() {
        // new int[100.0];
        indexList.add(new FloatLiteral(100));
        newArray = new NewArray(INT, indexList);
        Exception exception = assertThrows(ContextualError.class, () -> newArray.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "Index of array must be an integer";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyExprOverMaxDimensionError() {
        // new int[100][100][100];
        for (int i=0; i<3; i++) {
            indexList.add(new IntLiteral(100));
        }
        newArray = new NewArray(INT, indexList);
        Exception exception = assertThrows(ContextualError.class, () -> newArray.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "The dimension of an array cannot be greater than 2";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
