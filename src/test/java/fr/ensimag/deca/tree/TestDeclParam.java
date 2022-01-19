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
    AbstractIdentifier intId, voidId, name;

    @BeforeEach
    void setup() throws ContextualError, EnvironmentExp.DoubleDefException {
        compiler = new DecacCompiler(null,null);
        localEnv = new EnvironmentExp(null);

        intId = new Identifier(compiler.getSymbolTable().create("int"));
        voidId = new Identifier(compiler.getSymbolTable().create("void"));
        name = new Identifier(compiler.getSymbolTable().create("name"));
    }

    @Test
    public void testVerifyDeclParam() throws ContextualError {
        declParam = new DeclParam(intId, name);
        assertTrue(declParam.verifyDeclParam(compiler).isInt());
    }

    @Test
    public void testVerifyDeclParamVoidError(){
        declParam = new DeclParam(voidId, name);

        Exception exception = assertThrows(ContextualError.class, () -> {
            declParam.verifyDeclParam(compiler);
        });

        String expectedMessage = "Void parameter is not allowed";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyParamEnvExp() throws ContextualError {
        declParam = new DeclParam(intId, name);
        declParam.verifyDeclParam(compiler);
        declParam.verifyParamEnvExp(compiler, localEnv);
    }

    @Test
    public void testVerifyParamEnvExpDoubleDef() throws ContextualError {
        declParam = new DeclParam(intId, name);
        declParam.verifyDeclParam(compiler);
        declParam.verifyParamEnvExp(compiler, localEnv);

        Exception exception = assertThrows(ContextualError.class, () -> {
            declParam.verifyParamEnvExp(compiler, localEnv);
        });

        String expectedMessage = "Param identifier already declared";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
