package fr.ensimag.deca;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        String[] options = {"-b (banner) : affiche une bannière indiquant le nom de l’équipe",
        "-p (parse) : arrête decac après l’étape de construction de l’arbre, et affiche la décompilation de ce dernier (i.e. s’il n’y a qu’un fichier source à compiler, la sortie doit être un programme deca syntaxiquement correct)",
        "-v (verification) : arrête decac après l’étape de vérifications (ne produit aucune sortie en l’absence d’erreur)",
        "-n (no check) : supprime les tests à l’exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca.",
        "-r X (registers) : limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16",
        "-d (debug) : active les traces de debug. Répéter l’option plusieurs fois pour avoir plus de traces.",
        "-P (parallel) : s’il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle (pour accélérer la compilation)"};
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
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.

            // Nb threads = size of source files list
            // TODO: parallel execution
            ExecutorService executorService = Executors.newFixedThreadPool(options.getSourceFiles().size());
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                executorService.execute(() -> {
                    if (compiler.compile()) {
                        error[0] = true;
                    }
                });
            }
            executorService.shutdown();
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
