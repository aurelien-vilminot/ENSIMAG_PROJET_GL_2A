package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Print statement (print, println, ...).
 *
 * @author Aurélien VILMINOT
 * @date 04/01/2022
 */
public abstract class AbstractPrint extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify Print" + getSuffix() + ": start");

        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");

        for (AbstractExpr expr: this.arguments.getList()) {
            // Check if args types are printable
            Type argType = expr.verifyExpr(compiler, localEnv, currentClass);
            if (argType.isInt() || argType.isFloat() || argType.isString()) {
                expr.setType(argType);
            } else {
                throw new ContextualError("Impossible to print this type of element", this.getLocation());
            }
        }
        LOG.debug("verify Print" + getSuffix() + ": end");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (getPrintHex()) {
            for (AbstractExpr a : getArguments().getList()) {
                a.codeGenPrintx(compiler);
            }
        } else {
            for (AbstractExpr a : getArguments().getList()) {
                a.codeGenPrint(compiler);
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print"
                + getSuffix()
                + (getPrintHex() ? "x" : "")
                + "(");
        getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
