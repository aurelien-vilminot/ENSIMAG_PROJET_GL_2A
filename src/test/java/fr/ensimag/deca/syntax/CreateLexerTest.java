package fr.ensimag.deca.syntax;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CreateLexerTest {
    @Test
    public void testLexerBadInstantiation(){
        Logger.getRootLogger().setLevel(Level.WARN);
        String [] args = new String[2];

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        });

        String expectedMessage = "0 or 1 argument expected";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

}
