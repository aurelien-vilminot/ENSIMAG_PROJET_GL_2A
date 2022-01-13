package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestInitialization {
    @Test
    public void testSetExpression() {
        AbstractExpr int42 = new IntLiteral(42);
        AbstractExpr float42 = new FloatLiteral(42);
        Initialization initialization = new Initialization(int42);

        initialization.setExpression(float42);

        assertNotEquals(int42, initialization.getExpression());
        assertEquals(float42, initialization.getExpression());
    }
    
}
