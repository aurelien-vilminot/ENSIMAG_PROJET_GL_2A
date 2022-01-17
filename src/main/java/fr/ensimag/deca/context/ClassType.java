package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import org.apache.commons.lang.Validate;

import java.util.Objects;

/**
 * Type defined by a class.
 *
 * @author Aurélien VILMINOT
 * @date 04/01/2022
 */
public class ClassType extends Type {
    
    protected ClassDefinition definition;
    
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    

    @Override
    public boolean sameType(Type otherType) {
        Validate.notNull(otherType, "otherType object should not be null");
        return otherType.isClass();
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        Validate.notNull(potentialSuperClass, "The potential superclass should not be null");
        ClassDefinition currentClassDefinition = definition;
        // This loop follows all superclasses of this class.
        // It ends when the superclass is equals of the potential superclass
        while (!(currentClassDefinition == null)) {
            if (currentClassDefinition.equals(potentialSuperClass.definition)) {
                return true;
            }
            currentClassDefinition = currentClassDefinition.getSuperClass();
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassType classType = (ClassType) o;
        return Objects.equals(definition, classType.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(definition);
    }
}
