package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
     * @param compiler
     * @param n
     */
    protected abstract void codeGenStore(DecacCompiler compiler, int n);
}
