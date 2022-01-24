package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestDeclParam {
    DeclParam declParam;
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    AbstractIdentifier INT, VOID, name;

    @BeforeEach
    void setup() throws ContextualError, EnvironmentExp.DoubleDefException {
        compiler = new DecacCompiler(null,null);
        localEnv = new EnvironmentExp(null);

        INT = new Identifier(compiler.getSymbolTable().create("int"));
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        name = new Identifier(compiler.getSymbolTable().create("name"));
    }

    @Test
    public void testVerifyDeclParam() throws ContextualError {
        declParam = new DeclParam(INT, name);
        assertTrue(declParam.verifyDeclParam(compiler).isInt());
    }

    @Test
    public void testVerifyDeclParamVoidError(){
        declParam = new DeclParam(VOID, name);

        Exception exception = assertThrows(ContextualError.class, () -> declParam.verifyDeclParam(compiler));

        String expectedMessage = "Void type is not allowed for this parameter : void";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyParamEnvExp() throws ContextualError {
        declParam = new DeclParam(INT, name);
        declParam.verifyDeclParam(compiler);
        declParam.verifyParamEnvExp(compiler, localEnv);
    }

    @Test
    public void testVerifyParamEnvExpDoubleDef() throws ContextualError {
        declParam = new DeclParam(INT, name);
        declParam.verifyDeclParam(compiler);
        declParam.verifyParamEnvExp(compiler, localEnv);

        Exception exception = assertThrows(ContextualError.class, () -> declParam.verifyParamEnvExp(compiler, localEnv));

        String expectedMessage = "Param name 'name' already used";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
