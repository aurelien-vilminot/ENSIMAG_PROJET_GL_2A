package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.deca.codegen.LabelGenerator;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl07
 * @date 01/01/2022
 */
public class While extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label loop = LabelGenerator.newLabel(compiler);
        Label end = LabelGenerator.newLabel(compiler);

        compiler.addLabel(loop);

        condition.codeGenInst(compiler);; // (expected to charge boolean value on R0)
        compiler.addInstruction(new BNE(end));

        body.codeGenListInst(compiler);
        
        compiler.addInstruction(new BRA(loop));
        compiler.addLabel(end);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        LOG.debug("verify while: start");
        Validate.notNull(compiler, "Compiler (env_types) object should not be null");
        Validate.notNull(localEnv, "Env_exp object should not be null");
        Validate.notNull(returnType, "Return type should not be null");

        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
        LOG.debug("verify while: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
