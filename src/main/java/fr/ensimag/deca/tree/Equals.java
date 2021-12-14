package fr.ensimag.deca.tree;


/**
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }    
    
}
