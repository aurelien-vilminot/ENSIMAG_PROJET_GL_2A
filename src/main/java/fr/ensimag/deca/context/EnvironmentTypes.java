package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnvironmentTypes {
    private HashMap<Symbol, TypeDefinition> envTypes;

    public EnvironmentTypes() {
        this.envTypes = new HashMap<>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        Validate.notNull(key, "The symbol should not be null");
        if (this.envTypes.containsKey(key)) {
            // First, search in the current dictionary
            return this.envTypes.get(key);
        }
        // The symbol is undefined
        return null;
    }

    /**
     * Add the type definition associated to the symbol name in the environment.
     *
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the dictionary
     *
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws EnvironmentTypes.DoubleDefException
     *             if the symbol is already defined at the dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws EnvironmentTypes.DoubleDefException {
        Validate.notNull(name, "Symbol name should not be null");
        Validate.notNull(def, "Definition def should not be null");

        if (this.envTypes.containsKey(name)) {
            // The symbol is already defined in the current dictionary
            throw new EnvironmentTypes.DoubleDefException();
        } else {
            // Add the new association
            this.envTypes.put(name, def);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Environnement de types :");
        Set<Map.Entry<Symbol, TypeDefinition>> couples = this.envTypes.entrySet();
        for (Map.Entry<Symbol, TypeDefinition> couple : couples) {
            str.append("\n\t")
                    .append(couple.getKey())
                    .append(" : ")
                    .append(couple.getValue());
        }
        return str.toString();
    }

    /**
     * Check subtype relation of two classes
     * @param t1 The potential subtype
     * @param t2 The initial type
     * @return True if t1 is a subtype of t2
     */
    public boolean subTypes(Type t1, Type t2) {
        // if t1 and t2 are the same type, t1 is a subtype of t2
        if (t1.sameType(t2)) return true;
        if (t2.isClass()) {
            // For a class t2, null is always a subclass of t2
            if (t1.isNull()) return true;

            ClassType t1ClassType = (ClassType) t1;
            ClassType t2ClassType = (ClassType) t2;
            // Test if t1 is a subclass of t2
            return t1ClassType.isSubClassOf(t2ClassType);
        }
        return false;
    }

    public boolean assignCompatible(Type t1, Type t2) {
        return (t1.isFloat() && t2.isFloat()) || subTypes(t1, t2);
    }

    public boolean castCompatible(Type t1, Type t2) {
        return !t1.isVoid() && (assignCompatible(t1, t2) || assignCompatible(t2, t1));
    }
}