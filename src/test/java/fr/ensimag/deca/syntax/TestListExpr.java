package fr.ensimag.deca.syntax;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import fr.ensimag.deca.tree.ListExpr;
import fr.ensimag.deca.tree.StringLiteral;

public class TestListExpr {
    @Test
    public void testDecompileListEmpty(){
        ListExpr le = new ListExpr();
        assertEquals("", le.decompile());
    }

    @Test
    public void testDecompileListOneElement(){
        ListExpr le = new ListExpr();
        le.add(new StringLiteral("*"));
        assertEquals("\"*\"", le.decompile());
    }

    @Test
    public void testDecompileListMultipleElements(){
        ListExpr le = new ListExpr();
        le.add(new StringLiteral("1"));
        le.add(new StringLiteral("2"));
        le.add(new StringLiteral("3"));
        assertEquals("\"1\", \"2\", \"3\"", le.decompile());
    }

}
