package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDefinition {
    private static SymbolTable.Symbol symbol;

    @BeforeAll
    static void setup() {
        SymbolTable table = new SymbolTable();
        symbol = table.create("foo");
    }

    @Test
    public void testTypeDefinition() {
        TypeDefinition typeDefinition = new TypeDefinition(new NullType(symbol), null);

        // Check if is not an expression
        assertFalse(typeDefinition.isExpression());

        // Check nature
        assertEquals("type", typeDefinition.getNature());
    }

    @Test
    public void testExpDefinition() {
        ExpDefinition expDefinition = new VariableDefinition(new NullType(symbol), null);
        DAddr addr = new RegisterOffset(0, null);

        // Check operand
        expDefinition.setOperand(addr);
        assertSame(addr, expDefinition.getOperand());
    }
}
