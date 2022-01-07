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
}
