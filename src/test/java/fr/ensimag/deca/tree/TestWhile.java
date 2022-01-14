package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestWhile {
    private While w;
    private AbstractExpr condition;
    private ListInst body;
    
    @BeforeEach
    void setup() {
        condition = new BooleanLiteral(true);
        body = new ListInst();
        w = new While(condition, body);
    }

    @Test
    public void testGetCondition() {
        assertEquals(condition, w.getCondition());
    }

    @Test
    public void testGetBody() {
        assertEquals(body, w.getBody());
    }
}
