package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class MethodAsmBody extends AbstractMethodBody{
    private static final Logger LOG = Logger.getLogger(Main.class);

    private final StringLiteral stringLiteral;

    public MethodAsmBody(StringLiteral stringLiteral) {
        Validate.notNull(stringLiteral);
        this.stringLiteral = stringLiteral;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        LOG.debug("verify MethodAsmBody: start");
        Validate.notNull(compiler, "Compiler object should not be null");

        this.stringLiteral.verifyExpr(compiler, localEnv, currentClass);

        LOG.debug("verify MethodAsmBody: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" asm(");
        stringLiteral.decompile(s);
        s.println(");");
    }

    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv) {
        String asm = stringLiteral.decompile();
        asm = asm.substring(1, asm.length() - 1);
        Scanner scanner = new Scanner(asm);
        while (scanner.hasNextLine()) {
            compiler.add(new InlinePortion(scanner.nextLine()));
        }
        scanner.close();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        stringLiteral.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        stringLiteral.iter(f);
    }
}
