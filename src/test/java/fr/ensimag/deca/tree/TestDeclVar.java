package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

public class TestDeclVar {

    @Test
    public void testVerifyDeclVarDoubleDef() throws ContextualError {

        DecacCompiler compiler = new DecacCompiler(null, null);
        EnvironmentExp localEnv = new EnvironmentExp(null);

        AbstractIdentifier type = new Identifier(
                compiler.getSymbolTable().create("int")
            );
        AbstractIdentifier varName = new Identifier(
                compiler.getSymbolTable().create("x")
            );
        AbstractInitialization initialization = new Initialization( new IntLiteral(42));

        DeclVar declVar = new DeclVar(type, varName, initialization);

        declVar.verifyDeclVar(compiler, localEnv, null);

        Exception exception = assertThrows(ContextualError.class, () -> {
            declVar.verifyDeclVar(compiler, localEnv, null);
        });

        String expectedMessage = "Variable name 'x' already declared";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
