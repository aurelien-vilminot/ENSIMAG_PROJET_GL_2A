package fr.ensimag.deca;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getParse() {
        return this.parse;
    }

    public boolean getVerification() {
        return this.verification;
    }

    public boolean getNoCheck() {
        return this.noCheck;
    }

    public int getArgsNumber() {
        return argsNumber;
    }

    public int getRegisterNumber() { return registerNumber ;}

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private boolean parse = false;
    private boolean verification = false;
    private boolean registerLimit = false;
    private boolean noCheck = false;
    private int registerNumber = 16;
    private boolean registerNumberSet = false;
    private int argsNumber = 0;

    
    public void parseArgs(String[] args) throws CLIException {
        // Convert args into list
        ArrayList<String> argsList = new ArrayList<>();
        Collections.addAll(argsList, args);
        this.argsNumber = args.length;

        // Add sources files to the files list
        Pattern filePattern = Pattern.compile("^.?/?.*?\\.deca$");
        Pattern registerPattern = Pattern.compile("^-r$");
        Pattern numberPattern = Pattern.compile("^\\d+$");
        Pattern bannerPattern = Pattern.compile("^-b$");
        Pattern parserPattern = Pattern.compile("^-p$");
        Pattern verificationPattern = Pattern.compile("^-v$");
        Pattern parallelismPattern = Pattern.compile("^-P$");
        Pattern debugPattern = Pattern.compile("^-d$");
        Pattern nocheckPattern = Pattern.compile("^-n$");
        Pattern lastPattern = null;
        for (String arg: argsList) {
            if (filePattern.matcher(arg).matches()) {
                File fileToAdd = new File(arg);
                if (!this.sourceFiles.contains(fileToAdd)) {
                    this.sourceFiles.add(fileToAdd);
                }
            } else if (registerPattern.matcher(arg).matches()) {
                this.registerLimit = true;
                lastPattern = registerPattern;
                continue;
            } else if (numberPattern.matcher(arg).matches()) {
                if (!this.registerLimit || lastPattern != registerPattern) {
                    throw new CLIException("decac : impossible to use a number as an argument without the option -r");
                }
                this.registerNumber = Integer.parseInt(arg);
                this.registerNumberSet = true;
                if (this.registerNumber < 4 || this.registerNumber > 16) {
                    throw new CLIException("decac : number of registers have to be between 4 and 16");
                }
            } else if (bannerPattern.matcher(arg).matches()) {
                this.printBanner = true;
            } else if (parserPattern.matcher(arg).matches()) {
                this.parse = true;
            } else if (verificationPattern.matcher(arg).matches()) {
                this.verification = true;
            } else if (parallelismPattern.matcher(arg).matches()) {
                this.parallel = true;
            } else if (debugPattern.matcher(arg).matches()) {
                this.debug++;
            } else if (nocheckPattern.matcher(arg).matches()){
                this.noCheck = true;
            } else {
                throw new CLIException("decac : invalid option -- '" + arg + "'\nUsage :");
            }
            lastPattern = null;
        }

        if (this.printBanner && this.argsNumber != 1) {
            throw new CLIException("Impossible to use the option -b with other option(s)");
        }

        if (this.registerLimit && !this.registerNumberSet) {
            throw new CLIException("Impossible to use the option -r without a specified number of registers");
        }

        // Check if options are incompatible
        if (this.parse && this.verification) {
            throw new CLIException("decac -p and -v options are incompatible");
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        DecacMain.printAvailableOptions();
    }
}
