package fr.ensimag.deca.tree;


/**
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
