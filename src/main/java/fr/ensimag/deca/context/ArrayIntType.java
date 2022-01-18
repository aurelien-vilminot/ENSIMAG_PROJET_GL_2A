package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

public class ArrayIntType extends Type {

    private final int dimension;
    public ArrayIntType(SymbolTable.Symbol name, int dimension) {
        super(name);
        this.dimension = dimension;
    }

    public int getDimension(){
        return this.dimension;
    }

    @Override
    public boolean isArrayInt() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        Validate.notNull(otherType, "otherType object should not be null");
        if(otherType.isArrayInt()){
            Validate.isTrue(otherType instanceof ArrayIntType, "otherType object should be a class of type ArrayFloatType");
            return this.dimension == ((ArrayIntType) otherType).getDimension();
        }
        return false;
    }
}