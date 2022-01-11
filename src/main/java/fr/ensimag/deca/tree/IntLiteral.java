package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author Aurélien VILMINOT
 * @date 04/01/2022
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;
    private static final Logger LOG = Logger.getLogger(Main.class);

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify IntegerLiteral: start");

        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type intType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int")).getType();
        this.setType(intType);

        LOG.debug("verify IntegerLiteral: end");
        return intType;
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
