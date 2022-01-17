package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;

    // TODO: visibility
    public DeclField(AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        // TODO: LOAD value into R0
        // -2(LB) is the address of the object to initialize
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        DAddr dAddr = compiler.getEnvironmentExp().get(fieldName.getName()).getOperand();
        compiler.addInstruction(new STORE(Register.R0, dAddr));

    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO: visibility
        type.decompile(s);
        s.print(" ");
        fieldName.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }
}
