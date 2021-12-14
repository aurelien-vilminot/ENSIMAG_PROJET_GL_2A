package fr.ensimag.deca.tree;


/**
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
