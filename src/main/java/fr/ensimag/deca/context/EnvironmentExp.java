package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.commons.lang.Validate;

import java.util.*;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author Aurélien VILMINOT
 * @date 04/01/2022
 */
public class EnvironmentExp {

    EnvironmentExp parentEnvironment;
    private HashMap<Symbol, LinkedList<ExpDefinition>> associationTable;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.associationTable = new HashMap<>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        Validate.notNull(key, "The symbol should not be null");
        if (this.associationTable.containsKey(key)) {
            // First, search in the current dictionary
            return this.associationTable.get(key).getFirst();
        } else if (this.parentEnvironment != null &&
                this.parentEnvironment.associationTable.containsKey(key)) {
            // Search in the parent environment if key is not found in the current dictionary
            return this.parentEnvironment.associationTable.get(key).getFirst();
        }
        // The symbol is undefined
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        Validate.notNull(name, "Symbol name should not be null");
        Validate.notNull(def, "Definition def should not be null");

        if (this.associationTable.containsKey(name)) {
            // The symbol is already defined in the current dictionary
            throw new DoubleDefException();
        } else if (this.parentEnvironment != null && this.parentEnvironment.associationTable.containsKey(name)) {
            // Get the previous symbol declaration in parent environment
            LinkedList<ExpDefinition> expDefinitionLinkedList = new LinkedList<>(this.parentEnvironment.associationTable.get(name));
            // Add the new declaration at the top of the linked-list in the current directory
            expDefinitionLinkedList.addFirst(def);
            this.associationTable.put(name, expDefinitionLinkedList);
        } else {
            // Add the new association
            LinkedList<ExpDefinition> expDefinitionLinkedList = new LinkedList<>();
            expDefinitionLinkedList.addFirst(def);
            this.associationTable.put(name, expDefinitionLinkedList);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Environnement d'identificateur : \n");
        Set<Map.Entry<Symbol, LinkedList<ExpDefinition>>> couples = this.associationTable.entrySet();
        for (Map.Entry<Symbol, LinkedList<ExpDefinition>> couple : couples) {
            str.append("\t")
                    .append(couple.getKey())
                    .append(" : ");

            for (ExpDefinition expDefinition: couple.getValue()) {
                str.append("\n\t\t").append(expDefinition);
            }
        }

        return str.toString();
    }
}
