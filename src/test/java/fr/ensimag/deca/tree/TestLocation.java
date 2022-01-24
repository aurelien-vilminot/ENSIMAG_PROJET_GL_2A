package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestLocation {
    @Test
    public void testGetFilenameNull() {
        Location loc = new Location(0, 0, null);
        assertEquals(Location.NO_SOURCE_NAME, loc.getFilename());
    }
}