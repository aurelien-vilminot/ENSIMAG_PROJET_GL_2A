package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestEnvironmentExp {

    private EnvironmentExp environmentExpParent;
    private SymbolTable symbolTable;
    private Symbol symbolTest;
    private FieldDefinition fieldDefinition;

    @BeforeEach
    public void setup() {
        this.environmentExpParent = new EnvironmentExp(null);
        this.symbolTable = new SymbolTable();
        this.symbolTest = symbolTable.create("test");
        this.fieldDefinition = new FieldDefinition(null, null, null, null, -1);
    }

    @Test
    public void testGetDefinition() throws EnvironmentExp.DoubleDefException {
        // Check validate
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentExpParent.get(null);
        });
        String expectedMessage = "The symbol should not be null";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check unknown symbol
        assertNull(this.environmentExpParent.get(this.symbolTest));

        // Check known symbol
        this.environmentExpParent.declare(this.symbolTest, this.fieldDefinition);
        assertSame(this.fieldDefinition, this.environmentExpParent.get(symbolTest));

        // Create child environment and a new symbol
        EnvironmentExp environmentExpChild = new EnvironmentExp(this.environmentExpParent);
        Symbol symbolFake = this.symbolTable.create("fake");

        // Check symbol unknown in parent and current environment
        assertNull(environmentExpChild.get(symbolFake));

        // Check symbol know in parent environment
        assertSame(this.fieldDefinition, environmentExpChild.get(symbolTest));
    }

    @Test
    public void testDeclareDefinition() throws EnvironmentExp.DoubleDefException {
        // Check validate
        Exception exception;
        String expectedMessage, actualMessage;
        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentExpParent.declare(null, this.fieldDefinition);
        });
        expectedMessage = "Symbol name should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        exception = assertThrows(IllegalArgumentException.class, () -> {
            this.environmentExpParent.declare(this.symbolTest, null);
        });
        expectedMessage = "Definition def should not be null";
        actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check insertion in the parent environment
        assertDoesNotThrow(() -> {
            this.environmentExpParent.declare(this.symbolTest, this.fieldDefinition);
        });

        // Check double symbol declarations
        assertThrows(EnvironmentExp.DoubleDefException.class, () -> {
            this.environmentExpParent.declare(this.symbolTest, this.fieldDefinition);
        });

        // Create child environment and a new ExpDefinition
        EnvironmentExp environmentExpChild = new EnvironmentExp(this.environmentExpParent);
        FieldDefinition fieldDefinitionChild = new FieldDefinition(null, null, null, null, -1);
        environmentExpChild.declare(this.symbolTest, fieldDefinitionChild);

        // Check symbol staking
        assertNotSame(this.environmentExpParent.get(this.symbolTest), environmentExpChild.get(this.symbolTest));
        assertSame(environmentExpChild.get(this.symbolTest), fieldDefinitionChild);
    }
}
