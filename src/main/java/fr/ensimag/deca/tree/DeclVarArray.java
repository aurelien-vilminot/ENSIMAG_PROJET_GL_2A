package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * @author gl07
 * @date 01/01/2022
 */
public class DeclVarArray extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;
    final private int dimension;

    public DeclVarArray(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization, int dimension) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        Validate.notNull(dimension);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
        this.dimension = dimension;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    // TODO : Test the function
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        int i = 0;
        while(i<dimension){
            s.print("[]");
            i = i + 1;
        }
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
