package calc;

// ANTLR generated code uses the ANTLR runtime
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Example program using ANTLR. This is a simple calculator that
 * manages integer values, +, - and * operators (with correct
 * management of operators precedence).
 */
public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Enter expression (end with Ctrl-d):");

        // Instantiate lexer and parser, connected together:
        CalcLexer lexer =
            new CalcLexer(new ANTLRInputStream(System.in));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalcParser parser = new CalcParser(tokens);
        // Launch the parser
        AbstractExpr expression = parser.expr().tree;
        if (parser.getNumberOfSyntaxErrors() > 0) {
            System.out.println("Cannot compute expression because of syntax error (see above).");
        } else {
            // Display the result (e.g. Result: 1 + 2 * 3 = 7)
            System.out.println("Result : "
                    + expression.toString() + " = "
                    + expression.value());            
        }
    }
}
