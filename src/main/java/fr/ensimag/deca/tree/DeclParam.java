package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
        Validate.notNull(compiler);

        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError(
                    "Void type is not allowed for this parameter : " + this.type.getName(),
                    this.getLocation()
            );
        }
        this.name.setType(type);
        LOG.debug("verify DeclParam: end");
        return type;
    }

    @Override
    protected void verifyParamEnvExp(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        LOG.debug("verify ParamEnvExp: start");
        Validate.notNull(compiler);
        Validate.notNull(localEnv);

        ExpDefinition paramDefinition = new ParamDefinition(this.name.getType(), this.getLocation());

        try {
            localEnv.declare(this.name.getName(), paramDefinition);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Param name '" + this.name.getName() + "' already used", this.getLocation());
        }

        // Check param definition
        this.name.verifyExpr(compiler, localEnv, null);

        LOG.debug("verify ParamEnvExp: end");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
    }

    protected void codeGenDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv, int index) {
        localEnv.get(name.getName()).setOperand(new RegisterOffset(index, Register.LB));
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
    }
}
