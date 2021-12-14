package fr.ensimag.deca.tree;


/**
 * Operator "x >= y"
 * 
 * @author gl07
 * @date 01/01/2022
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
