package fr.ensimag.deca.syntax;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import fr.ensimag.deca.tree.StringLiteral;

public class TestStringLiteral {
    @Test
    public void testGetValue() {
        String value = "value";
        StringLiteral sl = new StringLiteral(value);
        assertEquals(value, sl.getValue());
    }

    @Test
    public void testDecompile() {
        String value = "value";
        StringLiteral sl = new StringLiteral(value);
        assertEquals("\"value\"", sl.decompile());
    }
}
