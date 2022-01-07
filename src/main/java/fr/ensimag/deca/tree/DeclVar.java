package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl07
 * @date 01/01/2022
 */
public class DeclVar extends AbstractDeclVar {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        LOG.debug("verify DeclVar: start");
        Type currentType = this.type.verifyType(compiler);
        if (currentType.isVoid()) {
            throw new ContextualError("Void cannot be the type of a variable", this.getLocation());
        }
        this.initialization.verifyInitialization(compiler, currentType, localEnv, currentClass);

        // Declare the new variable
        try {
            localEnv.declare(this.varName.getName(), new VariableDefinition(currentType, this.getLocation()));
        } catch (EnvironmentExp.DoubleDefException doubleDefException) {
            throw new ContextualError("Undeclared identifier", this.getLocation());
        }
        LOG.debug("verify DeclVar: end");
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
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
