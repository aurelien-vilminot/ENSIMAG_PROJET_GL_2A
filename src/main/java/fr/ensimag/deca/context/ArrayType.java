package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

public class ArrayType extends Type {

    private final int dimension;
    public ArrayType(SymbolTable.Symbol name, int dimension) {
        super(name);
        this.dimension = dimension;
    }

    @Override
    public boolean sameType(Type otherType) {
        Validate.notNull(otherType, "otherType object should not be null");
        return otherType.getName() == super.getName();
    }

}