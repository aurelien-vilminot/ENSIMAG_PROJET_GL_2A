package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    private final AbstractIdentifier type;
    private final AbstractIdentifier name;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        Validate.notNull(type);
        Validate.notNull(name);
        this.type = type;
        this.name = name;
    }

    @Override
    protected Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify DeclParam: start");

        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError("Void parameter is not allowed", this.getLocation());
        }
        this.name.setType(type);
        LOG.debug("verify DeclParam: end");
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
