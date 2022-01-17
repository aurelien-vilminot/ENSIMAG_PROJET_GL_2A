package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.RTS;
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
                                    this.superClass.getClassDefinition()
                            ),
                            this.getLocation(),
                            this.superClass.getClassDefinition()
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

        EnvironmentExp environmentExpSuperClass = ((ClassDefinition) compiler.getEnvironmentTypes().get(this.superClass.getName())).getMembers();
        EnvironmentExp environmentExpClass = ((ClassDefinition) compiler.getEnvironmentTypes().get(this.name.getName())).getMembers();

        try {
            environmentExpClass.addSuperExpDefinition(environmentExpSuperClass);
        } catch (EnvironmentExp.DoubleDefException e) {
            System.out.println("AAAA");
            // TODO
        }

        this.listDeclField.verifyListDeclField(compiler, this.superClass.getName(), this.name.getName());
        this.listDeclMethod.verifyListDeclMethod(compiler, this.superClass.getName());

        LOG.debug("verify ClassMembers: end");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify ClassBody: start");

        // Fields initialization
        this.listDeclField.verifyListInitField(compiler, this.name.getName());

        // Methods body
        // TODO

        LOG.debug("verify ClassBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        s.print(" extends ");
        superClass.decompile(s);
        s.print(" {");
        s.indent();
        listDeclField.decompile(s);
        listDeclMethod.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        // TODO: move to pass 1
        // Allocate pointer to superclass + @Objet.equals
        int addr = compiler.incGlobalStackSize(1);
        // TODO: @Object.equals
        DAddr dAddr = new RegisterOffset(addr, Register.GB);
        ((ClassDefinition) (compiler.getEnvironmentTypes().get(name.getName()))).setOperand(dAddr);
        compiler.incGlobalStackSize(name.getClassDefinition().getNumberOfMethods() + 1);
        // TODO: @superclass

        // CodeGen
        // init.name
        compiler.addLabel(new Label("init." + name.getName().toString()));
        // TODO: TSTO
        // TODO: BOV stack_overflow
        // TODO: ADDSP
        // TODO: save registers used over R2
        // initialisation des attributs (à 0 si non précisé)
        // listDeclField.codeGenListDeclField(compiler);
        // TODO: restore registers used over R2
        // return
        compiler.addInstruction(new RTS());
        // table des méthodes (code.name.methodname)
        // listDeclMethod.codeGenListDeclMethod(compiler);
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
