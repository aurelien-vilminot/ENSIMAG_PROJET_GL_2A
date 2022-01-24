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
 * @author gl07
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
        // Pass 1
        classes.verifyListClass(compiler);
        // Pass 2
        classes.verifyListClassMembers(compiler);
        // Pass 3
        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        classes.codeGenMethodTable(compiler);
        main.codeGenMain(compiler);
        classes.codeGenListDeclClass(compiler);
        this.codeGenInit(compiler);
        this.codeGenError(compiler);
    }

    /**
     * Generate assembly code for the first lines of the program
     *
     * @param compiler Corresponds to the "env_exp" attribute
     */
    protected void codeGenInit(DecacCompiler compiler) {
        compiler.addFirst(new Line(new ADDSP(compiler.getGlobalStackSize())));
        compiler.addStackOverflowError(true);
        compiler.addFirst(new Line(new TSTO(compiler.getGlobalStackSize() + compiler.getTempStackMax())));
        compiler.addFirst(new Line("Main program"));
    }

    /**
     * Generate assembly code for the main errors
     *
     * @param compiler Deca Compiler used to add IMA instructions
     */
    protected void codeGenError(DecacCompiler compiler) {
        compiler.addComment("Main errors");
        LabelGenerator gen = compiler.getLabelGenerator();
        if (gen.getOverflowError()) {
            gen.generateErrorLabel(compiler, gen.getOverFlowLabel(), "Error: overflow during arithmetic operation");
        }
        if (gen.getStackOverflowError()) {
            gen.generateErrorLabel(compiler, gen.getStackOverFlowLabel(), "Error: stack overflow");
        }
        if (gen.getHeapOverflowError()) {
            gen.generateErrorLabel(compiler, gen.getHeapOverFlowLabel(), "Error: Heap Overflow");
        }
        if (gen.getIoError()) {
            gen.generateErrorLabel(compiler, gen.getIoLabel(), "Error: input/output error");
        }
        if (gen.getDereferenceError()) {
            gen.generateErrorLabel(compiler, gen.getDereferenceLabel(), "Error: null dereference");
        }
        if (gen.getIndexOutOfBoundsError()) {
            gen.generateErrorLabel(compiler, gen.getIndexOutOfBoundsLabel(), "Error: index out of bounds");
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
