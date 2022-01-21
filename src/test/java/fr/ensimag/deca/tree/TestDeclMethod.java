package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class TestDeclMethod {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    AbstractIdentifier VOID, INT;
    DeclMethod declMethod;
    SymbolTable.Symbol superClassSymbol, classSymbol;
    ListDeclParam listDeclParam;

    @BeforeEach
    void setup() throws EnvironmentTypes.DoubleDefException {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        INT = new Identifier(compiler.getSymbolTable().create("int"));

        // Define a class
        superClassSymbol = compiler.getSymbolTable().create("Object");
        classSymbol = compiler.getSymbolTable().create("Pangolin");
        ClassType pangolinType = new ClassType(classSymbol, null, null);
        ClassDefinition classDefinition = new ClassDefinition(pangolinType,null,null);
        compiler.getEnvironmentTypes().declare(classSymbol, classDefinition);
    }

    @Test
    public void testVerify() throws ContextualError {
        // Add parameter declaration to list
        listDeclParam = new ListDeclParam();
        Identifier arg1 = new Identifier(compiler.getSymbolTable().create("arg1"));
        listDeclParam.add(new DeclParam(INT,arg1));

        // Create method declaration ( void f(int arg1); )
        declMethod = new DeclMethod(
                VOID,
                new Identifier(compiler.getSymbolTable().create("f")),
                listDeclParam,
                new MethodBody(new ListDeclVar(), new ListInst())
        );

        declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol);
        declMethod.verifyMethodBody(compiler, classSymbol);
    }

    // TODO: Test errors from verifyDeclMethod()

}
