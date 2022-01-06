package fr.ensimag.deca.syntax;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import fr.ensimag.deca.tree.Println;
import fr.ensimag.deca.tree.ListExpr;


public class TestPrintln {
    @Test
    public void testDecompilePrintln() {
        Println println = new Println(false, new ListExpr());
        assertEquals("println();", println.decompile());
    }

    @Test
    public void testDecompilePrintlnx() {
        Println printlnx = new Println(true, new ListExpr());
        assertEquals("printlnx();", printlnx.decompile());
    }
}
