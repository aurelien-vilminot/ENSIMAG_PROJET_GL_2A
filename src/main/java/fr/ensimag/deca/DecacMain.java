package fr.ensimag.deca;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void printAvailableOptions() {
        String[] options = {"-b (banner) : show the team banner",
        "-p (parse) : stop decac after the tree construction, and show its decompilation (i.e. if there is only one source file, the output must be a deca program with a valid syntax)",
        "-v (verification) : stop decac after the verification step (no output if there is no error)",
        "-n (no check) : ignore the tests during execution specified from 11.1 to 11.3 in the [Semantique] section",
        "-r X (registers) : limit the available registers to R0, ..., R{X-1}, with 4 <= X <= 16",
        "-d (debug) : activate debug traces. Repeat the option multiple times for more traces.",
        "-P (parallel) : if there are multiple source files, launch the parallel compilation of those files (to speed up compilation)"};
        for (String s : options) {
            System.out.println(s);
        }
    }

    public static void printBanner() {
        String banner =
                "  ______ ____  _    _ _____ _____  ______   ______ \n" +
                " |  ____/ __ \\| |  | |_   _|  __ \\|  ____| |____  |\n" +
                " | |__ | |  | | |  | | | | | |__) | |__        / / \n" +
                " |  __|| |  | | |  | | | | |  ___/|  __|      / /  \n" +
                " | |___| |__| | |__| |_| |_| |    | |____    / /   \n" +
                " |______\\___\\_\\\\____/|_____|_|    |______|  /_/ ";
        System.out.println(banner);
    }
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        final boolean[] error = {false};
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println(e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            // For decac -b option
            printBanner();
            System.exit(0);
        }

        if (options.getArgsNumber() == 0) {
            printAvailableOptions();
        }

        if (options.getParallel()) {
            int nThreads = Integer.min(options.getSourceFiles().size(), Runtime.getRuntime().availableProcessors());
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                executorService.execute(compiler);
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                error[0] = true;
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error[0] = true;
                }
            }
        }
        System.exit(error[0] ? 1 : 0);
    }
}
