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

public class TestSelection {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    Selection selection;
    SymbolTable.Symbol superClassSymbol, classSymbol, objectClassSymbol, undefClassSymbol, otherClassSymbol;
    AbstractIdentifier VOID, INT, BOOL;
    AbstractIdentifier publicField, protectedField;
    ClassDefinition classDefinition, superClassDefinition, otherClassDefinition;
    @Mock AbstractExpr pangolinInstance, superPangolinInstance, undefClassInstance, otherInstance;

    @BeforeEach
    void setup() throws EnvironmentTypes.DoubleDefException, ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        VOID = new Identifier(compiler.getSymbolTable().create("void"));
        INT = new Identifier(compiler.getSymbolTable().create("int"));
        BOOL = new Identifier(compiler.getSymbolTable().create("bool"));

        // Class SuperPangolin { protected int y; }
        // Class Pangolin extends SuperPangolin { public int x; }
        objectClassSymbol = compiler.getSymbolTable().create("Object");
        superClassSymbol = compiler.getSymbolTable().create("SuperPangolin");
        classSymbol = compiler.getSymbolTable().create("Pangolin");

        // Class SuperPangolin
        ClassType superPangolinType = new ClassType(superClassSymbol, Location.BUILTIN, null);
        superClassDefinition = new ClassDefinition(superPangolinType,Location.BUILTIN,null);
        compiler.getEnvironmentTypes().declare(superClassSymbol, superClassDefinition);
        when(superPangolinInstance.verifyExpr(compiler, localEnv, null)).thenReturn(superPangolinType);
        when(superPangolinInstance.getType()).thenReturn(superPangolinType);

        // SuperPagolin.y
        protectedField = new Identifier(compiler.getSymbolTable().create("y"));
        AbstractInitialization initialization = new Initialization(new IntLiteral(0));
        DeclField declField = new DeclField(INT, protectedField, initialization, Visibility.PROTECTED);
        declField.verifyDeclField(compiler, objectClassSymbol, superClassSymbol);

        // Class Pangolin
        ClassType pangolinType = new ClassType(classSymbol, Location.BUILTIN, superClassDefinition);
        classDefinition = new ClassDefinition(pangolinType,Location.BUILTIN, superClassDefinition);
        compiler.getEnvironmentTypes().declare(classSymbol, classDefinition);
        when(pangolinInstance.verifyExpr(eq(compiler), eq(localEnv), any(ClassDefinition.class))).thenReturn(pangolinType);
        when(pangolinInstance.getType()).thenReturn(pangolinType);

        // Pangolin.x
        publicField = new Identifier(compiler.getSymbolTable().create("x"));
        declField = new DeclField(INT, publicField, initialization, Visibility.PUBLIC);
        declField.verifyDeclField(compiler, superClassSymbol, classSymbol);

        // Class Undefined
        undefClassSymbol = compiler.getSymbolTable().create("Undefined");
        ClassType undefClassType = new ClassType(undefClassSymbol, null, null);
        when(undefClassInstance.verifyExpr(compiler, localEnv, null)).thenReturn(undefClassType);
        when(undefClassInstance.getType()).thenReturn(undefClassType);

        // Class Canard
        otherClassSymbol = compiler.getSymbolTable().create("Canard");
        ClassType canardType = new ClassType(superClassSymbol, Location.BUILTIN, null);
        otherClassDefinition = new ClassDefinition(canardType, Location.BUILTIN,null);
        compiler.getEnvironmentTypes().declare(otherClassSymbol, otherClassDefinition);
    }

    @Test
    public void testVerifyExprPublicFieldInMain() throws ContextualError {
        // Access public field x from Pangolin in main
        selection = new Selection(pangolinInstance, publicField);
        assertTrue(selection.verifyExpr(compiler, localEnv, null).isInt());
    }

    @Test
    public void testVerifyExprProtectedFieldInSubclass() throws ContextualError {
        // Access protected field y from SuperPangolin in Pangolin
        selection = new Selection(pangolinInstance, protectedField);
        assertTrue(selection.verifyExpr(compiler, localEnv, classDefinition).isInt());
    }

    @Test
    public void testVerifyExprUndefinedClassError() {
        selection = new Selection(undefClassInstance, publicField);
        Exception exception = assertThrows(ContextualError.class, () -> selection.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "Can't select field from non-class type : " + this.undefClassInstance.getType().getName();
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyExprProtectedFieldInMainError() {
        selection = new Selection(pangolinInstance, protectedField);
        Exception exception = assertThrows(ContextualError.class, () -> selection.verifyExpr(compiler, localEnv, null));

        String expectedMessage = "The identifier '" + protectedField.getName() + "' is protected and it is impossible to access it in the main program";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testVerifyExprImpossibleToAccess() {
        selection = new Selection(pangolinInstance, protectedField);
        Exception exception = assertThrows(ContextualError.class, () -> selection.verifyExpr(compiler, localEnv, otherClassDefinition));

        String expectedMessage = "Impossible to select this identifier : " + this.protectedField.getName();
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
