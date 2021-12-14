package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;

/**
 * Exception raised when a #include is found for a file that cannot be found or opened.
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class IncludeFileNotFound extends DecaRecognitionException {
    private final String name;

    public IncludeFileNotFound(String name, AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return name + ": include file not found";
    }
    
    private static final long serialVersionUID = -8541996188279897766L;

}
