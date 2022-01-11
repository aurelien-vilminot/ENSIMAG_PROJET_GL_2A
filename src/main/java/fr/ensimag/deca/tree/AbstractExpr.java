package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        LOG.debug("verify RValue: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");
        Validate.notNull(expectedType, "Expected type should not be null");

        Type currentType = this.verifyExpr(compiler, localEnv, currentClass);

        // Check type compatibility
        boolean areCompatible = compiler.getEnvironmentTypes().assignCompatible(expectedType, currentType);
        if (!areCompatible) {
            throw new ContextualError("Types are not compatible", this.getLocation());
        }
        this.setType(currentType);
        LOG.debug("verify RValue: end");
        return this;
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify Inst: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        this.setType(this.verifyExpr(compiler, localEnv, currentClass));
        LOG.debug("verify Inst: end");
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify Condition: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");

        Type currentType = this.verifyExpr(compiler, localEnv, currentClass);

        // Check if it is a boolean expression
        if (!currentType.isBoolean()) {
            throw new ContextualError("Expression type must be boolean", this.getLocation());
        }

        LOG.debug("verify Condition: end");
    }

    protected void codeGenPrint(DecacCompiler compiler, boolean printHex) {
        Instruction outputInstruction = this.outputExpr(printHex);
        if (outputInstruction != null) {
            // Generate code to load expression in R1
            this.codeGenExpr(compiler, 1);
            // Output R1
            compiler.addInstruction(outputInstruction);
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        codeGenPrint(compiler, false);
    }

    /**
     * Generate code to print the expression using hexadecimal value for floats
     *
     * @param compiler
     */
    protected void codeGenPrintx(DecacCompiler compiler) {
        codeGenPrint(compiler, true);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public DVal dval(DecacCompiler compiler) {
        return null;
    }

    public Instruction outputExpr(boolean printHex) {
        Type type = getType();
        if (type.isInt()) {
            return new WINT();
        } else if (type.isFloat()) {
            if (printHex) {
                return new WFLOATX();
            } else {
                return new WFLOAT();
            }
        }
        return null;
    }

    /**
     * Calculate and load an expression in the n-th register
     *
     * @param compiler
     * @param n
     */
    protected void codeGenExpr(DecacCompiler compiler, int n) {
        DVal dval = this.dval(compiler);
        if (dval != null) {
            compiler.addInstruction(new LOAD(dval, Register.getR(n)));
        }
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
