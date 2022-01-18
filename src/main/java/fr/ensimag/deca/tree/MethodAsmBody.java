package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody{
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final StringLiteral stringLiteral;

    public MethodAsmBody(StringLiteral stringLiteral) {
        Validate.notNull(stringLiteral);
        this.stringLiteral = stringLiteral;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        LOG.debug("verify MethodAsmBody: start");
        Validate.notNull(compiler, "Compiler object should not be null");

        this.stringLiteral.verifyExpr(compiler, localEnv, currentClass);

        LOG.debug("verify MethodAsmBody: end");
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
