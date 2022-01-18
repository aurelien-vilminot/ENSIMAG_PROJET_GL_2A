package fr.ensimag.deca.tree;

import com.sun.tools.javac.comp.Env;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.util.Iterator;

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

    protected EnvironmentExp verifyParamEnvExp(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        LOG.debug("verify ListParamEnvExp: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        EnvironmentExp environmentExpParam = new EnvironmentExp(localEnv);

        for (AbstractDeclParam abstractDeclParam : this.getList()) {
            abstractDeclParam.verifyParamEnvExp(compiler, environmentExpParam);
        }
        LOG.debug("verify ListParamEnvExp: end");
        return environmentExpParam;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclParam> ite = getList().iterator();
        if (ite.hasNext()) {
            ite.next().decompile(s);
        }
        while (ite.hasNext()) {
            s.print(", ");
            ite.next().decompile(s);
        }
    }

    protected void codeGenDeclMethod(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Class not yet implemented, or cast conversion is not allowed");
    }
}
