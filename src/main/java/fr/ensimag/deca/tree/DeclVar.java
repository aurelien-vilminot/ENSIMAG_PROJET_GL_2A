package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
    private ClassDefinition currentClass;

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
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        this.currentClass = currentClass;

        // Check type definition
        Type currentType = this.type.verifyType(compiler);
        if (currentType.isVoid()) {
            throw new ContextualError("Void cannot be the type of a variable", this.getLocation());
        }
        this.initialization.verifyInitialization(compiler, currentType, localEnv, currentClass);

        // Declare the new variable
        try {
            localEnv.declare(this.varName.getName(), new VariableDefinition(currentType, this.getLocation()));
        } catch (EnvironmentExp.DoubleDefException doubleDefException) {
            throw new ContextualError("Variable name '"+ this.varName.getName() + "' already declared", this.getLocation());
        }

        // Check var definition
        this.varName.verifyExpr(compiler, localEnv, currentClass);

        LOG.debug("verify DeclVar: end");
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler, EnvironmentExp localEnv) {
        DAddr dAddr;
        if (currentClass == null) {
            // Set operand global address
            int addr = compiler.incGlobalStackSize(1);
            dAddr = new RegisterOffset(addr, Register.GB);
        } else {
            // Set operand local address
            int addr = compiler.incLocalStackSize(1);
            dAddr = new RegisterOffset(addr, Register.LB);
        }

        localEnv.get(varName.getName()).setOperand(dAddr);
        // Generate code for initialization
        initialization.codeGenInit(compiler, dAddr);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
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
