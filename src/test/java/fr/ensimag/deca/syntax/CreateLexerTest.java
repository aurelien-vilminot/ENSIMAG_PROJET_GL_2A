package fr.ensimag.deca.syntax;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class CreateLexerTest {
    @Test
    public void testLexerInstantiationTooMuchArgs(){
        Logger.getRootLogger().setLevel(Level.WARN);
        String [] args = new String[2];

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        });

        String expectedMessage = "0 or 1 argument expected";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Hard to test because of stdin ...
     */
    /*
    @Test
    public void testLexerInstantiationTNoArgs () throws IOException {
        Logger.getRootLogger().setLevel(Level.WARN);
        String [] args = new String[0];
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
    }
    */

    @Test
    public void testLexerInstantiation() throws IOException{
        Logger.getRootLogger().setLevel(Level.WARN);
        String [] args = new String[1];
        args[0] = "./src/test/deca/syntax/valid/provided/hello.deca";
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        assertEquals(lex.getSourceName(), args[0]);
    }

}
