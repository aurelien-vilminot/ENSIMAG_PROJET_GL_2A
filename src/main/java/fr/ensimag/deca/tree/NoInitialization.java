package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl07
 * @date 01/01/2022
 */
public class NoInitialization extends AbstractInitialization {
    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify NonInitialization: start");
        // Nothing to do
        LOG.debug("verify NonInitialization: end");
    }

    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr dAddr) {
        // nothing
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n, Type type) {
        compiler.setAndVerifyCurrentRegister(n);

        if (type.isBoolean() || type.isInt()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.getR(n)));
        } else if (type.isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.getR(n)));
        } else if (type.isClass()) {
            compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(n)));
        }
    }

    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
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
