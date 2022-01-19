package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.Location;

public class RuntimeLocationException extends RuntimeException {
    public RuntimeLocationException(String message, Location location) {
        super(location.getFilename() + ":" + Integer.toString(location.getLine()) + ":" + location.getPositionInLine() + ": " + message);
    }
}
