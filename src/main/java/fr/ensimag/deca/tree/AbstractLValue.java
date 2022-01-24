package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractLValue extends AbstractExpr {

    /**
     * Generate code to store AbstractLValue in the n-th register
     * Right expression is supposed to be in the n-th register
     *
     * @param compiler //TODO
     * @param n
     */
    protected abstract void codeGenStore(DecacCompiler compiler, int n);
}
