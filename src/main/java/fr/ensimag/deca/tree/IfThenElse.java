package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Full if/else if/else statement.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class IfThenElse extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public void setElseBranch(ListInst elseBranch) {
        this.elseBranch = elseBranch;
    }

    public ListInst getElseBranch() {
        return elseBranch;
    }

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify ifThenElse: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
//        Validate.notNull(localEnv, "Env_exp object should not be null");
        Validate.notNull(returnType, "Return type should not be null");

        this.condition.verifyInst(compiler, localEnv, currentClass, returnType);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        LOG.debug("verify ifThenElse: else");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label elseLabel = new Label(compiler.getLabelGenerator().generateLabel("else"));
        Label endLabel = new Label(compiler.getLabelGenerator().generateLabel("end"));

        // Generate code for condition
        this.condition.codeGenExprBool(compiler, false, elseLabel, 2);
        // Generate code for instruction(s)
        this.thenBranch.codeGenListInst(compiler);
        // Go to the end of if statement after the instruction execution
        compiler.addInstruction(new BRA(endLabel));
        // Add the else label
        compiler.addLabel(elseLabel);
        // Gen code for else branch which could be contained other ifThenElse branches
        this.elseBranch.codeGenListInst(compiler);
        // Add the end label
        compiler.addLabel(endLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.print("}");
        if (!elseBranch.isEmpty()) {
            s.println(" else {");
            s.indent();
            elseBranch.decompile(s);
            s.unindent();
            s.print("}");
        }
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
