package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.IntStream;

/**
 * Exception raised when a chain of #include is circular, i.e. a file
 * includes a file which has already been included.
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class CircularInclude extends DecaRecognitionException {
    private static final long serialVersionUID = -3517868082633812254L;
    private final String name;

    public CircularInclude(String name, AbstractDecaLexer recognizer, IntStream input) {
        super(recognizer, input);
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Circular include for file " + name;
    }
}
