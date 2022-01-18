package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final ListDeclVar listDeclVar;
    private final ListInst listInst;

    public MethodBody(ListDeclVar listDeclVar, ListInst listInst) {
        Validate.notNull(listDeclVar);
        Validate.notNull(listInst);
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        LOG.debug("verify MethodBody: start");
        Validate.notNull(compiler, "Compiler object should not be null");

        this.listDeclVar.verifyListDeclVariable(compiler, localEnv, currentClass);
        this.listInst.verifyListInst(compiler, localEnv, currentClass, returnType);

        LOG.debug("verify MethodBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println(" {");
        s.indent();
        listDeclVar.decompile(s);
        listInst.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        listDeclVar.iter(f);
        listInst.iter(f);
    }
}
