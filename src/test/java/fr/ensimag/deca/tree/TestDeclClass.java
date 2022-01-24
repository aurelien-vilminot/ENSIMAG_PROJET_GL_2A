package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeclClass {
    private static DecacCompiler compiler;
    private static Identifier identifierClass;
    private static Identifier identifierSuperClass;
    private static ListDeclMethod listDeclMethod;
    private static ListDeclField listDeclField;

    @BeforeAll
    public static void setup() throws EnvironmentTypes.DoubleDefException {
        compiler = new DecacCompiler(null, null);
        SymbolTable.Symbol symbol1 = compiler.getSymbolTable().create("foo");
        SymbolTable.Symbol symbol2 = compiler.getSymbolTable().create("foo2");

        ClassDefinition classDefinitionSuperClass = new ClassDefinition(
                new ClassType(symbol2, null, null),
                null,
                null
        );

        ClassDefinition classDefinitionClass = new ClassDefinition(
                new ClassType(symbol1, null, classDefinitionSuperClass),
                null,
                classDefinitionSuperClass
        );

        compiler.getEnvironmentTypes().declare(symbol2, classDefinitionSuperClass);
        compiler.getEnvironmentTypes().declare(symbol1, classDefinitionClass);
        identifierClass = new Identifier(symbol1);
        identifierSuperClass = new Identifier(symbol2);

        listDeclField = new ListDeclField();
        listDeclMethod = new ListDeclMethod();
    }

    @Test
    public void testVerifyClass() throws EnvironmentTypes.DoubleDefException {
        // Check validate
        DeclClass declClass = new DeclClass(identifierClass, identifierSuperClass, listDeclField, listDeclMethod);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> declClass.verifyClass(null));
        assertEquals("Compiler (env_types) object should not be null", exception.getMessage());

        // Check super-class with a bad type (int)
        SymbolTable.Symbol intSymbol = compiler.getSymbolTable().create("bar");
        TypeDefinition intTypeDefinition = new TypeDefinition(new IntType(intSymbol), null);
        compiler.getEnvironmentTypes().declare(intSymbol, intTypeDefinition);
        Identifier intIdentifier= new Identifier(intSymbol);
        DeclClass declClass1 = new DeclClass(identifierClass, intIdentifier, listDeclField, listDeclMethod);
        exception = assertThrows(ContextualError.class, () -> declClass1.verifyClass(compiler));
        assertEquals("The super-class name is a not a class : " + intSymbol, exception.getMessage());

        // Check already declared class identifier
        exception = assertThrows(ContextualError.class, () -> declClass.verifyClass(compiler));
        assertEquals(
                "The class name '"+ identifierClass.getName() + "' is already declared",
                exception.getMessage()
        );
    }
}
