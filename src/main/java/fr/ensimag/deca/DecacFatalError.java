package fr.ensimag.deca;

/**
 * Exception raised when something prevents the compilation (e.g.
 * source file unreadable).
 *
 * @author gl07
 * @date 01/01/2022
 */
public class DecacFatalError extends Exception {
    private static final long serialVersionUID = 6141682385316820966L;

    public DecacFatalError(String message) {
        super(message);
    }
}
