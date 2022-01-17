package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassPass1: start");
        for (AbstractDeclClass abstractDeclClass : this.getList()) {
            abstractDeclClass.verifyClass(compiler);
        }
        LOG.debug("verify listClassPass1: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassPass2: start");
        for (AbstractDeclClass abstractDeclClass : this.getList()) {
            abstractDeclClass.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassPass2: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 1 of [CodeGen]
     * @param compiler
     */
    protected void codeGenMethodTable(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 2 of [CodeGen]
     * @param compiler
     */
    protected void codeGenListDeclClass(DecacCompiler compiler) {
        for (AbstractDeclClass c : getList()) {
            c.codeGenDeclClass(compiler);
        }
    }

}
