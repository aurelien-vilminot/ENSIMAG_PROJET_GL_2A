package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

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
        return this.verfication;
    }

    public int getArgsNumber() {
        return argsNumber;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private boolean parse = false;
    private boolean verfication = false;
    private int argsNumber = 0;

    
    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.

        // Convert args into list
        ArrayList<String> argsList = new ArrayList<>();
        Collections.addAll(argsList, args);
        this.argsNumber = argsList.size();

        // Allow banner print
        if (argsList.contains("-b")) {
            if (argsList.size() > 1) {
                throw new CLIException("Impossible to use decac -b with another option");
            } else {
                this.printBanner = true;
            }
        }

        // Check if some options are incompatible
        if (argsList.contains("-p") && argsList.contains("-v")) {
            throw new CLIException("decac -p and -v options are incompatible");
        }

        if (argsList.contains("-p")) {
            this.parse = true;
        }

        if (argsList.contains("-v")) {
            this.verfication = true;
        }

        // Allow debug traces
        if (argsList.contains("-d")) {
            Set<String> unique = new HashSet<String>(argsList);
            for (String key : unique) {
                if (key.equals("-d")) {
                    // Count -d number, more there are more traces are
                    this.debug = Collections.frequency(argsList, key);
                }
            }
        }

        // Allow parallelism
        if (argsList.contains("-P")) {
            this.parallel = true;
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
        throw new UnsupportedOperationException("not yet implemented");
    }
}
