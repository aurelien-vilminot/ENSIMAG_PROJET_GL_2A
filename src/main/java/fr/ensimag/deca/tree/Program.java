package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelGenerator;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author Aurélien VILMINOT
 * @date 04/01/2022
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        Validate.notNull(compiler, "Compiler object should not be null");
        // TODO: reactivate pass 2 after hello world
        // Pass 1
        classes.verifyListClass(compiler);
        // Pass 2
        classes.verifyListClassMembers(compiler);
        // Pass 3
//        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // classes.codeGenMethodTable(compiler);
        classes.codeGenListDeclClass(compiler);
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        this.codeGenInit(compiler);
        this.codeGenError(compiler);
    }

    protected void codeGenInit(DecacCompiler compiler) {
        compiler.addFirst(new Line(new ADDSP(compiler.getGlobalStackSize())));
        compiler.addStackOverflowError(true);
        compiler.addFirst(new Line(new TSTO(compiler.getGlobalStackSize() + compiler.getTempStackMax())));
        compiler.addFirst(new Line("Main program"));
    }

    protected void codeGenError(DecacCompiler compiler) {
        compiler.addComment("Main errors");
        LabelGenerator gen = compiler.getLabelGenerator();
        if (gen.getOverflowError()) {
            compiler.getLabelGenerator().generateErrorLabel(compiler, gen.getOverFlowLabel(), "Error: Overflow during arithmetic operation");
        }
        if (gen.getStackOverflowError()) {
            compiler.getLabelGenerator().generateErrorLabel(compiler, gen.getStackOverFlowLabel(), "Error: Stack Overflow");
        }
        if (gen.getIoError()) {
            compiler.getLabelGenerator().generateErrorLabel(compiler, gen.getIoLabel(), "Error: Input/Output error");
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
