package fr.ensimag.deca.tree;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

public class TestAssign {
    final Type INT = new IntType(null);
    final Type FLOAT = new FloatType(null);

    private DecacCompiler compiler;
    private EnvironmentExp localEnv;
    private AbstractLValue leftOperand;
    private AbstractExpr rightOperand;
    private Assign assign;

    @BeforeEach
    void setup() throws DoubleDefException {
        compiler = new DecacCompiler(null, null);
        leftOperand = new Identifier(compiler.getSymbolTable().create("x"));
        localEnv = new EnvironmentExp(null);
        localEnv.declare(
            compiler.getSymbolTable().create("x"),
            new VariableDefinition(FLOAT, Location.BUILTIN)
        );
    }

    @Test
    public void testVerifyExprSameType() throws ContextualError {
        rightOperand = new FloatLiteral(0);
        assign = new Assign(leftOperand, rightOperand);
        assertTrue(assign.verifyExpr(compiler, localEnv, null).isFloat());
    }

    @Test
    public void testVerifyExprFloatInt() throws ContextualError {
        rightOperand = new IntLiteral(0);
        assign = new Assign(leftOperand, rightOperand);
        assertTrue(assign.verifyExpr(compiler, localEnv, null).isFloat());
    }

    @Test
    public void testVerifyExprWrongType() throws ContextualError {
        rightOperand = new StringLiteral("42.0");
        assign = new Assign(leftOperand, rightOperand);

        Exception exception = assertThrows(ContextualError.class, () -> {
            assign.verifyExpr(compiler, localEnv, null);
        });

        String expectedMessage = "Types are not compatible";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
