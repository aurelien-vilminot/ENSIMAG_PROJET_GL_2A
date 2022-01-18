package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl07
 * @date 01/01/2022
 */
public abstract class AbstractInitialization extends Tree {
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Calculate and store an expression in the specified memory location
     *
     * @param compiler Deca Compiler used to add IMA instruction
     * @param dAddr The address where the expression must be load
     */
    protected abstract void codeGenInit(DecacCompiler compiler, DAddr dAddr);

    /**
     * Calculate and load the initialized expression in the n-th register
     * If initialization is null (NoInitialization), load a null value
     * in the n-th register (0 for int, false for boolean...)
     *
     * @param compiler
     * @param n
     */
    protected abstract void codeGenExpr(DecacCompiler compiler, int n, Type type);
}
