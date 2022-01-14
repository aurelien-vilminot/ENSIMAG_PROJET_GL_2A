package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ensimag.deca.tools.DecacInternalError;

public class TestTree {
    Tree tree;

    @BeforeEach
    void setup() {
        tree = new IntLiteral(0);
    }

    @Test
    public void testCheckLocationNullError() {
        tree.setLocation(null);
        Exception exception = assertThrows(DecacInternalError.class, () -> {
            tree.checkLocation();
        });

        String expectedMessage = "Tree " + tree.getClass().getName() + " has no location set";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testSetLocationInFile() {
        tree.setLocation(0,0,"Foo.java");
        Location location = tree.getLocation();

        assertEquals(0,location.getLine());
        assertEquals(0,location.getPositionInLine());
        assertEquals("Foo.java",location.getFilename());
    }
}
