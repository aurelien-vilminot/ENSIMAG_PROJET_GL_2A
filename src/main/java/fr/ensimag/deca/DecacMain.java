package fr.ensimag.deca;

import java.io.File;
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
        String banner = "BANNIERE PAR DEFAUT\t|\tG2/GL07";
        System.out.println(banner);
    }
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            throw new UnsupportedOperationException("decac -b not yet implemented");
        }
        if (options.getSourceFiles().isEmpty()) {
            printAvailableOptions();
        }
        // TODO: -p, -v, -n, -r, -d, -P
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
