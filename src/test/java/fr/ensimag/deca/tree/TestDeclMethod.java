package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestDeclMethod {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    AbstractIdentifier VOID, INT;
    DeclMethod declMethod;
    SymbolTable.Symbol superClassSymbol, classSymbol, objectClassSymbol;
    ClassDefinition classDefinition, superClassDefinition;

    @BeforeEach
    void setup() throws EnvironmentTypes.DoubleDefException {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        INT = new Identifier(compiler.getSymbolTable().create("int"));

        // Class SuperPangolin {  }
        // Class Pangolin extends SuperPangolin {  }
        objectClassSymbol = compiler.getSymbolTable().create("Object");
        superClassSymbol = compiler.getSymbolTable().create("SuperPangolin");
        classSymbol = compiler.getSymbolTable().create("Pangolin");

        // Class SuperPangolin
        ClassType superPangolinType = new ClassType(superClassSymbol, Location.BUILTIN, null);
        superClassDefinition = new ClassDefinition(superPangolinType,Location.BUILTIN,null);
        compiler.getEnvironmentTypes().declare(superClassSymbol, superClassDefinition);

        // Class Pangolin
        ClassType pangolinType = new ClassType(classSymbol, Location.BUILTIN, superClassDefinition);
        classDefinition = new ClassDefinition(pangolinType,Location.BUILTIN, superClassDefinition);
        compiler.getEnvironmentTypes().declare(classSymbol, classDefinition);
    }

    @Test
    public void testVerify() throws ContextualError {
        int numMethods = classDefinition.getNumberOfMethods();

        // Add parameter declaration to list
        ListDeclParam listDeclParam = new ListDeclParam();
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

        assertEquals(numMethods+1, classDefinition.getNumberOfMethods());
    }

    @Test
    public void testVerifyOverride() throws ContextualError {
        int numMethods = classDefinition.getNumberOfMethods();

        // Create method declaration ( void f(); {})
        declMethod = new DeclMethod(
                VOID,
                new Identifier(compiler.getSymbolTable().create("f")),
                new ListDeclParam(),
                new MethodBody(new ListDeclVar(), new ListInst())
        );

        // Declare method in super class
        declMethod.verifyDeclMethod(compiler, objectClassSymbol, superClassSymbol);
        // Declare method in child class
        declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol);

        assertEquals(numMethods, classDefinition.getNumberOfMethods());
    }

    @Test
    public void testVerifyOverrideDifferentSignatureError() throws ContextualError {
        Identifier f = new Identifier(compiler.getSymbolTable().create("y"));
        ListDeclParam listDeclParam = new ListDeclParam();

        // Create method declaration (int f() {})
        declMethod = new DeclMethod(
                INT,
                f,
                listDeclParam,
                new MethodBody(new ListDeclVar(), new ListInst())
        );
        // Declare method in super class
        declMethod.verifyDeclMethod(compiler, objectClassSymbol, superClassSymbol);

        // Add parameter to method declaration signature ( int f(int x) {})
        listDeclParam.add(new DeclParam(INT,new Identifier(compiler.getSymbolTable().create("x"))));

        // Declare method in child class
        Exception exception = assertThrows(ContextualError.class, () -> declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Method prototype must be same as inherited";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyOverrideDifferentReturnTypeError() throws ContextualError {
        Identifier f = new Identifier(compiler.getSymbolTable().create("y"));

        // Create method declaration (int f() {})
        declMethod = new DeclMethod(
                INT,
                f,
                new ListDeclParam(),
                new MethodBody(new ListDeclVar(), new ListInst())
        );
        // Declare method in super class
        declMethod.verifyDeclMethod(compiler, objectClassSymbol, superClassSymbol);

        // Create method declaration (void f() {})
        declMethod = new DeclMethod(
                VOID,
                f,
                new ListDeclParam(),
                new MethodBody(new ListDeclVar(), new ListInst())
        );

        // Declare method in child class
        Exception exception = assertThrows(ContextualError.class, () -> declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Return type must be a subtype of inherited method return";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyOverrideFieldError() throws ContextualError {
        Identifier f = new Identifier(compiler.getSymbolTable().create("y"));

        // Create field declaration (int f = 0;)
        DeclField declField = new DeclField(
                INT,
                f,
                new Initialization(new IntLiteral(0)),
                Visibility.PUBLIC
        );
        // Declare field in super class
        declField.verifyDeclField(compiler, objectClassSymbol, superClassSymbol);


        // Create method declaration (int f(); {})
        declMethod = new DeclMethod(
                INT,
                f,
                new ListDeclParam(),
                new MethodBody(new ListDeclVar(), new ListInst())
        );

        // Declare method in child class
        Exception exception = assertThrows(ContextualError.class, () -> declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Super-class symbol must be a method definition";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyDoubleDefError() throws ContextualError {
        declMethod = new DeclMethod(
                VOID,
                new Identifier(compiler.getSymbolTable().create("f")),
                new ListDeclParam(),
                new MethodBody(new ListDeclVar(), new ListInst())
        );

        declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol);

        Exception exception = assertThrows(ContextualError.class, () -> declMethod.verifyDeclMethod(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Method name 'f' already declared in the class";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    // TODO: Test errors from verifyDeclMethod()

}
