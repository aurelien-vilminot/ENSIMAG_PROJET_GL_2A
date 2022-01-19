package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl07
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final AbstractIdentifier name;
    private final AbstractIdentifier superClass;
    private ListDeclField listDeclField;
    private ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier name,
                     AbstractIdentifier superClass,
                     ListDeclField listDeclField,
                     ListDeclMethod listDeclMethod) {
        Validate.notNull(name);
        Validate.notNull(superClass);
        Validate.notNull(listDeclField);
        Validate.notNull(listDeclMethod);
        this.name = name;
        this.superClass = superClass;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify DeclClass: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        // Check type of super-class identifier
        TypeDefinition superClassType = compiler.getEnvironmentTypes().get(this.superClass.getName());
        if (superClassType == null || !superClassType.isClass()) {
            throw new ContextualError("Expected class identifier", this.getLocation());
        }

        // Add new class if not already existed
        try {
            compiler.getEnvironmentTypes().declare(
                    this.name.getName(),
                    new ClassDefinition(
                            new ClassType(
                                    this.name.getName(),
                                    this.getLocation(),
                                    (ClassDefinition) superClassType
                            ),
                            this.getLocation(),
                            (ClassDefinition) superClassType
                    )
            );
        } catch (EnvironmentTypes.DoubleDefException e) {
            throw new ContextualError("Already class identifier declared", this.getLocation());
        }

        // Tree decoration for classes identifiers
        this.superClass.verifyType(compiler);
        this.name.verifyType(compiler);

        LOG.debug("verify DeclClass: end");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        LOG.debug("verify ClassMembers: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        ClassDefinition currentClassDefinition = (ClassDefinition) compiler.getEnvironmentTypes().get(this.name.getName());
        ClassDefinition superClassDefinition = (ClassDefinition) compiler.getEnvironmentTypes().get(this.superClass.getName());

        // Increment fields and methods number for current class depending on super-class members
        currentClassDefinition.setNumberOfFields(superClassDefinition.getNumberOfFields());
        currentClassDefinition.setNumberOfMethods(superClassDefinition.getNumberOfMethods());

        this.listDeclField.verifyListDeclField(compiler, this.superClass.getName(), this.name.getName());
        this.listDeclMethod.verifyListDeclMethod(compiler, this.superClass.getName(), this.name.getName());

        LOG.debug("verify ClassMembers: end");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify ClassBody: start");

        // Fields initialization
        this.listDeclField.verifyListInitField(compiler, this.name.getName());

        // Methods body
        this.listDeclMethod.verifyListMethodBody(compiler, this.superClass.getName(), this.name.getName());

        LOG.debug("verify ClassBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        s.print(" extends ");
        superClass.decompile(s);
        s.println(" {");
        s.indent();
        listDeclField.decompile(s);
        listDeclMethod.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void codeGenMethodTable(DecacCompiler compiler) {
        // Allocate pointer to superclass
        int index = compiler.incGlobalStackSize(1);
        DAddr dAddr = new RegisterOffset(index, Register.GB);
        name.getClassDefinition().setOperand(dAddr);
        DAddr superClassDaddr = superClass.getClassDefinition().getOperand();
        // Load @superClass inside dAddr
        compiler.addInstruction(new LEA(superClassDaddr, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, dAddr));
        // Generate virtual methods table
        listDeclMethod.codeGenMethodTable(compiler, name, superClass);
    }

    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        compiler.addLabel(new Label("init." + name.getName().toString()));

        // Create a new IMAProgram to be able to add instructions at the beginning of the block
        IMAProgram backupProgram = compiler.getProgram();
        IMAProgram initClassProgram = new IMAProgram();
        compiler.setProgram(initClassProgram);

        // Call parent init
        if (superClass.getClassDefinition().getNumberOfFields() != 0 ) {
            // Initialize fields address, and store default value if superClass has fields
            // TODO: not optimized ?
            listDeclField.codeGenListDeclFieldDefault(compiler);
            compiler.addInstruction(new PUSH(Register.R1));
            compiler.addInstruction(new BSR(new Label("init."+superClass.getName().getName())));
            // TODO: verify why SUBSP #1
            compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
        }

        // Initialize fields
        compiler.setMaxUsedRegister(0);
        compiler.setTempStackMax(0);
        listDeclField.codeGenListDeclField(compiler);

        // Restore registers
        compiler.restoreRegisters();

        // Return
        compiler.addInstruction(new RTS());

        // Instructions added at the beginning of the block
        compiler.saveRegisters();
        int d = compiler.getNumberOfRegistersUsed() + compiler.getTempStackMax();
        if (d > 0) {
            compiler.addStackOverflowError(true);
            compiler.addFirst(new Line(new TSTO(new ImmediateInteger(d))));
        }
        // Restore initial IMAProgram
        backupProgram.append(initClassProgram);
        compiler.setProgram(backupProgram);
        compiler.setMaxUsedRegister(0);
        compiler.setTempStackMax(0);

        // Generate method instruction (code.name.methodname)
        listDeclMethod.codeGenListDeclMethod(compiler);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        superClass.prettyPrint(s, prefix, false);
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        superClass.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

}
