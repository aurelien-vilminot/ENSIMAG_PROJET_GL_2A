package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ReadInt extends AbstractReadExpr {
    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify ReadInt: start");

        Type intType = compiler.getEnvironmentTypes().get(compiler.getSymbolTable().create("int")).getType();
        this.setType(intType);

        LOG.debug("verify ReadInt: end");
        return intType;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        // R1 <- readInt()
        compiler.addInstruction(new RINT());
        // Rn <- V[R1]
        compiler.addInstruction(new LOAD(Register.getR(1), Register.getR(n)));
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
