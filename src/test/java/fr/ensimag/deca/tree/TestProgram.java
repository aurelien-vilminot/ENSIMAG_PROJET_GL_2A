package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestProgram {

    Program program;

    @Mock
    ListDeclClass classes;

    @Mock
    AbstractMain main;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        program = new Program(classes, main);
    }

    @Test
    public void testGetClasses() {
        assertEquals(classes, program.getClasses());
    }

    @Test
    public void testGetMain() {
        assertEquals(main, program.getMain());
    }
}
