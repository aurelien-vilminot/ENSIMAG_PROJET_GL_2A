package fr.ensimag.deca;

import fr.ensimag.deca.codegen.LabelGenerator;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.BOV;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl07
 * @date 01/01/2022
 */
public class DecacCompiler implements Runnable {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    private EnvironmentExp environmentExp;
    private EnvironmentTypes environmentTypes;
    private SymbolTable symbolTable = new SymbolTable();

    private int globalStackSize = 0; // number of global variables
    private int tempStackCurrent = 0; // current temporary stack usage
    private int tempStackMax = 0; // maximal temporary stack usage

    public int getGlobalStackSize() {
        return globalStackSize;
    }

    public int incGlobalStackSize(int inc) {
        Validate.isTrue(inc >= 0, "The incrementation should be positive");
        globalStackSize += inc;
        return globalStackSize;
    }

    public int incTempStackCurrent(int inc) {
        tempStackCurrent += inc;
        return tempStackCurrent;
    }

    public void setTempStackMax() {
        tempStackMax = Integer.max(tempStackMax, tempStackCurrent);
    }

    public int getTempStackMax() {
        return tempStackMax;
    }


    private final LabelGenerator labelGenerator = new LabelGenerator();

    public LabelGenerator getLabelGenerator() {
        return this.labelGenerator;
    }

    public void addOverflowError() {
        addOverflowError(false);
    }

    public void addStackOverflowError(boolean first) {
        if (!this.compilerOptions.getNoCheck()) {
            if (first) {
                addFirst(new Line(new BOV(getLabelGenerator().getStackOverFlowLabel())));
            } else {
                addInstruction(new BOV(getLabelGenerator().getStackOverFlowLabel()));
            }
        }
    }

    public void addOverflowError(boolean first) {
        if (!this.compilerOptions.getNoCheck()) {
            if (first) {
                addFirst(new Line(new BOV(getLabelGenerator().getOverFlowLabel())));
            } else {
                addInstruction(new BOV(getLabelGenerator().getOverFlowLabel()));
            }
        }
    }

    public void addIoError() {
        if (!this.compilerOptions.getNoCheck()) {
            addInstruction(new BOV(getLabelGenerator().getIoLabel()));
        }
    }

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        if (compilerOptions == null) {
            this.compilerOptions = new CompilerOptions();
        } else {
            this.compilerOptions = compilerOptions;
        }
        this.source = source;

        // Init environments
        this.environmentExp = new EnvironmentExp(null);
        this.environmentTypes = new EnvironmentTypes();

        // Declare init symbols
        Symbol voidSymbol = this.symbolTable.create("void");
        Symbol booleanSymbol = this.symbolTable.create("boolean");
        Symbol floatSymbol = this.symbolTable.create("float");
        Symbol intSymbol = this.symbolTable.create("int");
        Symbol objectSymbol = this.symbolTable.create("Object");
        Symbol equalsSymbol = this.symbolTable.create("equals");

        // Define default types environment
        try {
            this.environmentTypes.declare(voidSymbol, new TypeDefinition(new VoidType(voidSymbol), Location.BUILTIN));
            this.environmentTypes.declare(booleanSymbol, new TypeDefinition(new BooleanType(booleanSymbol), Location.BUILTIN));
            this.environmentTypes.declare(floatSymbol, new TypeDefinition(new FloatType(floatSymbol), Location.BUILTIN));
            this.environmentTypes.declare(intSymbol, new TypeDefinition(new IntType(intSymbol), Location.BUILTIN));
            this.environmentTypes.declare(objectSymbol, new ClassDefinition(
                    new ClassType(objectSymbol, Location.BUILTIN, null),
                    Location.BUILTIN,
                    null
            ));
        } catch (EnvironmentTypes.DoubleDefException doubleDefException) {
            LOG.error("Multiple type declaration");
        }

        // Init equals method
        Signature equalsSignature = new Signature();
        MethodDefinition equalsDefinition = new MethodDefinition(
                this.environmentTypes.get(booleanSymbol).getType(),
                Location.BUILTIN,
                equalsSignature,
                0
        );
        try {
            this.environmentExp.declare(equalsSymbol, equalsDefinition);
        } catch (EnvironmentExp.DoubleDefException doubleDefException) {
            LOG.error("Multiple type declaration");
        }
    }

    public EnvironmentExp getEnvironmentExp() {
        return environmentExp;
    }

    public EnvironmentTypes getEnvironmentTypes() {
        return environmentTypes;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addFirst(fr.ensimag.ima.pseudocode.Line)
     */
    public void addFirst(Line l) {
        program.addFirst(l);
    }

    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = source.getAbsolutePath().replaceAll("\\.deca$", ".ass");
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());

        if (this.compilerOptions.getParse()) {
            // Display tree decompilation
            prog.decompile(out);
            return false;
        }

        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());

        if (this.compilerOptions.getVerification()) {
            // Stop program after syntax verification
            return false;
        }

        // addComment("start main program");
        prog.codeGenProgram(this);
        // addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    @Override
    public void run() {
        this.compile();
    }
}
