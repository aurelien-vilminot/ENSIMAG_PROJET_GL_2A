package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl07
 * @date 01/01/2022
 */
public class Identifier extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(Main.class);

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *     private static final Logger LOG = Logger.getLogger(Main.class);

     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Identifier: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        // Check if identifier is already declared
        ExpDefinition expDefinition = localEnv.get(this.name);
        if (expDefinition == null) {
            throw new ContextualError("Undeclared identifier : " + this.name, this.getLocation());
        } else {
            this.definition = expDefinition;
            this.setType(expDefinition.getType());
        }
        LOG.debug("verify Identifier: end");

        return this.definition.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Type: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");

        // Check if type identifier exists
        TypeDefinition currentType = compiler.getEnvironmentTypes().get(this.name);
        if (currentType == null) {
            throw new ContextualError("Undefined type identifier: " + this.name, this.getLocation());
        }
        this.definition = currentType;
        this.setType(currentType.getType());
        LOG.debug("verify Type: end");

        return currentType.getType();
    }

    @Override
    public MethodDefinition verifyMethod(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        LOG.debug("verify Method: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Local environment object should not be null");

        MethodDefinition methodDefinition = localEnv.get(this.getName())
                .asMethodDefinition("This identifier is not a method : " + this.getName(), this.getLocation());
        this.setDefinition(methodDefinition);
        LOG.debug("verify Method: end");

        return methodDefinition;
    }


    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    public DVal dval(DecacCompiler compiler) {
        return getExpDefinition().getOperand();
    }

    @Override
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        super.codeGenExpr(compiler, n);

        if (definition.isField()) {
            // if identifier is field, Rn contains its heap address
            int index = getFieldDefinition().getIndex();
            compiler.addInstruction(new LOAD(new RegisterOffset(index, Register.getR(n)), Register.getR(n)));
        }
    }

    @Override
    protected void codeGenStore(DecacCompiler compiler, int n) {
        int maxRegister = compiler.setAndVerifyCurrentRegister(n);

        DAddr dAddr = (DAddr) dval(compiler);
        if (definition.isField()) {
            int index = getFieldDefinition().getIndex();
            if (n < maxRegister) {
                // Calculate heap address of the object into Rn+1
                compiler.addInstruction(new LOAD(dAddr, Register.getR(n+1)));
                // Store into Rn the field
                compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(index, Register.getR(n+1))));
            } else {
                compiler.incTempStackCurrent(1);
                compiler.setTempStackMax();
                compiler.addInstruction(new PUSH(Register.getR(n)), "save");
                // Calculate heap address of the object into Rn
                compiler.addInstruction(new LOAD(dAddr, Register.getR(n)));

                compiler.addInstruction(new LOAD(Register.getR(n), Register.R0));
                // R0 contains heap address of the object
                compiler.addInstruction(new POP(Register.getR(n)), "restore");
                compiler.incTempStackCurrent(-1);
                // Load R0 into correct field in the object
                compiler.addInstruction(new STORE(Register.getR(n), new RegisterOffset(index, Register.R0)));
            }
        } else {
            compiler.addInstruction(new STORE(Register.getR(n), dAddr));
        }
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

}
