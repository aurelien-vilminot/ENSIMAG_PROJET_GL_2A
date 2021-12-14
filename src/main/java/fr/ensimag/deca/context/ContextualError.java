package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;

/**
 * Exception raised when a contextual error is found.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class ContextualError extends LocationException {
    private static final long serialVersionUID = -8122514996569278575L;

    public ContextualError(String message, Location location) {
        super(message, location);
    }
}
