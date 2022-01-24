package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PrintTest {
    @Test
    public void testPrintDecompile() {
        ListExpr arguments = new ListExpr();
        Print print = new Print(false, arguments);
        assertEquals(print.decompile(), "print();");

        ListExpr argumentsx = new ListExpr();
        Print printx = new Print(true, argumentsx);
        assertEquals(printx.decompile(), "printx();");
    }
}
