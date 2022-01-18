package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class ListDeclParam extends TreeList<AbstractDeclParam> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    protected Signature verifyDeclParam(DecacCompiler compiler)
            throws ContextualError {
        LOG.debug("verify ListDeclParam: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Signature signature = new Signature();

        for (AbstractDeclParam abstractDeclParam : this.getList()) {
            signature.add(abstractDeclParam.verifyDeclParam(compiler));
        }
        LOG.debug("verify ListDeclParam: end");
        return signature;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }
}
