package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private static final Logger LOG = Logger.getLogger(Main.class);

    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;
    private final Visibility visibility;

    public DeclField(AbstractIdentifier type,
                     AbstractIdentifier fieldName,
                     AbstractInitialization initialization,
                     Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        Validate.notNull(visibility);
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
        this.visibility = visibility;
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, SymbolTable.Symbol superSymbol, SymbolTable.Symbol symbolCurrentClass)
            throws ContextualError {
        LOG.debug("verify DeclField: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError("Void type cannot be declared as a field type", this.getLocation());
        }
        ClassDefinition currentClassDefinition = (ClassDefinition) compiler.getEnvironmentTypes().get(symbolCurrentClass);
        EnvironmentExp environmentExpCurrentClass = currentClassDefinition.getMembers();
        EnvironmentExp envExpName =((ClassDefinition) compiler.getEnvironmentTypes().get(superSymbol)).getMembers();

        if (envExpName.get(this.fieldName.getName()) != null && !envExpName.get(this.fieldName.getName()).isField()) {
            throw new ContextualError("Super-class redefinition identifier must be a field", this.getLocation());
        }

        currentClassDefinition.incNumberOfFields();

        try {
            environmentExpCurrentClass.declare(
                    this.fieldName.getName(),
                    new FieldDefinition(
                            type,
                            this.getLocation(),
                            this.visibility,
                            currentClassDefinition,
                            currentClassDefinition.getNumberOfFields()
                    )
            );
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError(
                    "Field name '"+ this.fieldName.getName() + "' already declared",
                    this.getLocation()
            );
        }

        this.fieldName.verifyExpr(compiler, environmentExpCurrentClass, currentClassDefinition);
        this.setVisibility(visibility);
        LOG.debug("verify DeclField: end");
    }

    @Override
    protected void verifyInitField(DecacCompiler compiler, SymbolTable.Symbol currentClass) throws ContextualError {
        LOG.debug("verify InitField: start");
        ClassDefinition currentClassDefinition = (ClassDefinition)compiler.getEnvironmentTypes().get(currentClass);
        this.initialization.verifyInitialization(
                compiler,
                this.type.getType(),
                currentClassDefinition.getMembers(),
                currentClassDefinition
        );
        LOG.debug("verify InitField: end");
    }

    @Override
    protected void codeGenDeclFieldDefault(DecacCompiler compiler) {
        if (type.getType().isBoolean() || type.getType().isInt()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        } else if (type.getType().isFloat()) {
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
        } else if (type.getType().isClass()) {
            compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        }
        int index = fieldName.getFieldDefinition().getIndex();
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        // Set operand
        DAddr dAddr = new RegisterOffset(-2, Register.LB);
        fieldName.getFieldDefinition().setOperand(dAddr);
        // Initialize expression in R0
        initialization.codeGenExpr(compiler, 0, type.getType());
        // -2(LB) is the address of the object to initialize
        compiler.addInstruction(new LOAD(dAddr, Register.R1));
        // index(R1) is the address of the current field
        int index = fieldName.getFieldDefinition().getIndex();
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, Register.R1)));

    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (visibility == Visibility.PUBLIC) {
            s.print("public ");
        } else {
            s.print("protected ");
        }
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
