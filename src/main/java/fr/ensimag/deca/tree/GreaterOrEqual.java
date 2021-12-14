package fr.ensimag.deca.tree;


/**
 * Operator "x >= y"
 * 
 * @author @AUTHOR@
 * @date @DATE@
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
