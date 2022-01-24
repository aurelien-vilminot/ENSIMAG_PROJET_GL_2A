package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestDeclField {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    SymbolTable.Symbol superClassSymbol, classSymbol;
    AbstractIdentifier VOID, INT;
    DeclField declField;
    AbstractIdentifier fieldName;
    AbstractInitialization initialization;
    ClassDefinition classDefinition, superClassDefinition;

    @BeforeEach
    void setup() throws EnvironmentTypes.DoubleDefException {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        INT = new Identifier(compiler.getSymbolTable().create("int"));

        // Define classes
        superClassSymbol = compiler.getSymbolTable().create("SuperPangolin");
        classSymbol = compiler.getSymbolTable().create("Pangolin");

        ClassType superPangolinType = new ClassType(superClassSymbol, null, null);
        superClassDefinition = new ClassDefinition(superPangolinType,null,null);
        compiler.getEnvironmentTypes().declare(superClassSymbol, superClassDefinition);

        ClassType pangolinType = new ClassType(classSymbol, null, superClassDefinition);
        classDefinition = new ClassDefinition(pangolinType,null, superClassDefinition);
        compiler.getEnvironmentTypes().declare(classSymbol, classDefinition);

        // Initialize field declaration parameters
        fieldName = new Identifier(compiler.getSymbolTable().create("x"));
        initialization = new Initialization(new IntLiteral(42));
    }

    @Test
    public void testVerifyDeclField() throws ContextualError {
        declField = new DeclField(INT, fieldName, initialization, Visibility.PUBLIC);
        int numFields = classDefinition.getNumberOfFields();
        declField.verifyDeclField(compiler, superClassSymbol, classSymbol);
        assertEquals(numFields+1, classDefinition.getNumberOfFields());
    }

    @Test
    public void testVerifyDeclFieldVoidTypeError() {
        declField = new DeclField(VOID, fieldName, initialization, Visibility.PUBLIC);

        Exception exception = assertThrows(ContextualError.class, () -> declField.verifyDeclField(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Void type cannot be declared as a field type";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyDeclFieldRedefinitionError() throws ContextualError {
        // The compiler is not supposed to accept a super method being redefined by a child field
        // Declare "void x(){};" in super class
        SymbolTable.Symbol objectClassSymbol = compiler.getSymbolTable().create("Object");
        DeclMethod declMethod = new DeclMethod(
                VOID,
                fieldName,
                new ListDeclParam(),
                new MethodBody(new ListDeclVar(), new ListInst())
        );
        declMethod.verifyDeclMethod(compiler, objectClassSymbol, superClassSymbol);

        // Declare "int x = 0;" in child class
        declField = new DeclField(INT, fieldName, initialization, Visibility.PUBLIC);
        Exception exception = assertThrows(ContextualError.class, () -> declField.verifyDeclField(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Super-class redefinition identifier must be a field";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyDeclFieldVDoubleDefError() throws ContextualError {
        declField = new DeclField(INT, fieldName, initialization, Visibility.PUBLIC);
        declField.verifyDeclField(compiler, superClassSymbol, classSymbol);

        Exception exception = assertThrows(ContextualError.class, () -> declField.verifyDeclField(compiler, superClassSymbol, classSymbol));

        String expectedMessage = "Field name '"+ fieldName.getName() + "' already declared";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyInitField() throws ContextualError {
        declField = new DeclField(INT, fieldName, initialization, Visibility.PUBLIC);
        declField.verifyDeclField(compiler, superClassSymbol, classSymbol);
        declField.verifyInitField(compiler, classSymbol);
    }
}
