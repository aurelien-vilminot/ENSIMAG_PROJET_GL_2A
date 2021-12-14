package fr.ensimag.ima.pseudocode;

/**
 * Internal error related to IMA code. Should never happen.
 * 
 * @author Ensimag
 * @date @DATE@
 */
public class IMAInternalError extends RuntimeException {
    public IMAInternalError(String message, Throwable cause) {
        super(message, cause);
    }

    public IMAInternalError(String message) {
        super(message);
    }

    private static final long serialVersionUID = 3929345355905773360L;

}
