package calc;

public abstract class AbstractExpr {
    /**
     * Compute the value of the expression as an Integer.
     */
    public abstract int value();

    /**
     * Display the expression as a string, e.g. 1 + 2 * 3
     */
    @Override
    public abstract String toString();
}
