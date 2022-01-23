package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Syntax error for an expression that should be an lvalue (ie that can be
 * assigned), but is not.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class DecaNumberFormatException extends DecaRecognitionException {

    private static final long serialVersionUID = -5925250548356056237L;

    private final String message;

    public DecaNumberFormatException(DecaParser recognizer, ParserRuleContext ctx, String message) {
        super(recognizer, ctx);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
