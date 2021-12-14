package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import java.util.BitSet;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.log4j.Logger;

/**
 * Error listener that stops the compilation at the first error usuing
 * ParseCancellationException.
 *
 * @author Ensimag
 */
public class DecacErrorListner implements ANTLRErrorListener {
    private static final Logger LOG = Logger.getLogger(DecacErrorListner.class);

    private IntStream input;

    public DecacErrorListner(IntStream input) {
        this.input = input;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        // When trying to recover from an error, e can actually be null
        // (eg. "println(;")
        Token offendingToken = null;
        if (e != null) {
            offendingToken = e.getOffendingToken();
        }
        Location l;
        if (offendingToken != null) {
            LOG.info("Use token info for Location");
            l = new Location(offendingToken.getLine(),
                offendingToken.getCharPositionInLine(),
                offendingToken.getTokenSource().getSourceName());
        } else if (e instanceof DecaRecognitionException) {
            LOG.info("no token => using e: location=" + ((DecaRecognitionException) e).getLocation());
            l = ((DecaRecognitionException) e).getLocation();
        } else {
            LOG.info("no token, no LocationException => using input.getSourceName(): line="
                    + line);
            String sourceName;
            if (recognizer instanceof AbstractDecaLexer) {
                // Can be different from input.getSourceName because of
                // #include.
                sourceName = ((AbstractDecaLexer) recognizer).getSourceName();
            } else {
                sourceName = input.getSourceName();
            }
            l = new Location(line, charPositionInLine, sourceName);
        }
        throw new ParseCancellationException(
                new LocationException(msg, l));
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
