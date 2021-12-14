package fr.ensimag.deca.syntax;

import java.io.IOException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author Ensimag
 * @date @DATE@
 */
public class ManualTestLex {
    // This is a test class, we do not try to give user-friendly error messages
    // but just throw exception to the user if something goes wrong (=> throws
    // IOException)
    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.DEBUG);
        DecaLexer lex = AbstractDecaLexer.createLexerFromArgs(args);
        System.exit(lex.debugTokenStream() ? 1 : 0);
    }
}
