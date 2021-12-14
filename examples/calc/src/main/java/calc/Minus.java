package calc;

public class Minus extends AbstractExpr {
    private final AbstractExpr left;
    private final AbstractExpr right;

    public Minus(AbstractExpr left, AbstractExpr right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public int value() {
        return left.value() - right.value();
    }

    @Override
    public String toString() {
        return left.toString() + " - " + right.toString();
    }

}
