package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TestEnvironmentTypes {

    private EnvironmentTypes environmentTypes;
    private SymbolTable symbolTable;
    private SymbolTable.Symbol symbolTest;
    private TypeDefinition typeDefinition;

    @BeforeEach
    public void setup() {
        this.environmentTypes = new EnvironmentTypes();
        this.symbolTable = new SymbolTable();
        this.symbolTest = symbolTable.create("test");
        this.typeDefinition = new TypeDefinition(null, null);
    }

    @Test
    public void testGetDefinition() throws EnvironmentTypes.DoubleDefException {
        // Check validate
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.get(null);
        });
        String expectedMessage = "The symbol should not be null";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check unknown symbol
        assertNull(this.environmentTypes.get(this.symbolTest));

        // Check known symbol
        this.environmentTypes.declare(this.symbolTest, this.typeDefinition);
        assertSame(this.typeDefinition, this.environmentTypes.get(symbolTest));
    }

    @Test
    public void testDeclareDefinition() throws EnvironmentTypes.DoubleDefException {
        // Check validate
        Exception exception;
        String expectedMessage, actualMessage;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.declare(null, this.typeDefinition);
        });
        expectedMessage = "Symbol name should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.declare(this.symbolTest, null);
        });
        expectedMessage = "Definition def should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check insertion
        assertDoesNotThrow(() -> {
            this.environmentTypes.declare(this.symbolTest, this.typeDefinition);
        });

        // Check double symbol declarations
        assertThrows(EnvironmentTypes.DoubleDefException.class, () -> {
            this.environmentTypes.declare(this.symbolTest, this.typeDefinition);
        });
    }

    @Test
    public void testSubTypes() {
        ClassType type1 = new ClassType(this.symbolTest, null, null);
        ClassType type2 = new ClassType(this.symbolTest, null, type1.getDefinition());

        // Check validate
        Exception exception;
        String expectedMessage, actualMessage;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.subTypes(null, type1);
        });
        expectedMessage = "Type t1 should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.subTypes(type1, null);
        });
        expectedMessage = "Type t2 should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check same type for type1 and type2
        assertTrue(this.environmentTypes.subTypes(type2, type1));
        assertTrue(this.environmentTypes.subTypes(type1, type1));

        // Check if null is a subtype of any class
        Type typeNull = new NullType(this.symbolTest);
        assertTrue(this.environmentTypes.subTypes(typeNull, type2));

        // Check if type2 is effectively a subclass of type2
        assertTrue(this.environmentTypes.subTypes(type2, type1));

        // Check with different types
        VoidType voidType = new VoidType(this.symbolTest);
        assertFalse(this.environmentTypes.subTypes(type1, voidType));
    }

    @Test
    public void testAssignCompatible() {
        FloatType type1 = new FloatType(this.symbolTest);
        IntType type2 = new IntType(this.symbolTest);

        // Check validate
        Exception exception;
        String expectedMessage, actualMessage;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.subTypes(null, type1);
        });
        expectedMessage = "Type t1 should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.subTypes(type1, null);
        });
        expectedMessage = "Type t2 should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check if float and int types are compatibles
        assertTrue(this.environmentTypes.assignCompatible(type1, type2));

        // Check if int types are compatibles
        assertTrue(this.environmentTypes.assignCompatible(type1, type1));

        // Check class and int types not compatibles
        ClassType classType = new ClassType(this.symbolTest);
        assertFalse(this.environmentTypes.assignCompatible(classType, type1));
    }

    @Test
    public void testCastCompatible() {
        FloatType type1 = new FloatType(this.symbolTest);
        IntType type2 = new IntType(this.symbolTest);

        // Check validate
        Exception exception;
        String expectedMessage, actualMessage;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.subTypes(null, type1);
        });
        expectedMessage = "Type t1 should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentTypes.subTypes(type1, null);
        });
        expectedMessage = "Type t2 should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check if float and int types could be cast
        assertTrue(this.environmentTypes.castCompatible(type1, type2));

        // Check if int and float types could be cast
        assertTrue(this.environmentTypes.castCompatible(type2, type1));

        // Check if void type is not castable
        VoidType voidType = new VoidType(this.symbolTest);
        assertFalse(this.environmentTypes.castCompatible(voidType, type2));
    }

    @Test
    public void testToString() throws EnvironmentTypes.DoubleDefException {
        // Check empty environment
        String expectedString = "Environnement de types :";
        assertEquals(expectedString, this.environmentTypes.toString());

        this.environmentTypes.declare(this.symbolTest, this.typeDefinition);

        // Test toString type return
        assertInstanceOf(String.class, this.environmentTypes.toString());

        // Check environment with declared definition
        System.out.println(this.environmentTypes);
        expectedString =
                "Environnement de types :\n" +
                        "\ttest : type defined at null, type=null";
        assertEquals(expectedString, this.environmentTypes.toString());
    }

}
