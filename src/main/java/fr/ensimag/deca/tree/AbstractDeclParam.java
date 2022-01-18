package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;

public abstract class AbstractDeclParam extends Tree {
    // TODO: comment
    protected abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;

}
