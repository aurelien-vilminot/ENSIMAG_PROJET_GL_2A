package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

public class ArrayFloatType extends Type {

    private final int dimension;
    public ArrayFloatType(SymbolTable.Symbol name, int dimension) {
        super(name);
        this.dimension = dimension;
    }

    public int getDimension(){
        return this.dimension;
    }

    @Override
    public boolean isArrayFloat() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        Validate.notNull(otherType, "otherType object should not be null");
        if(otherType.isArrayFloat()){
            Validate.isTrue(otherType.getClass().getName().equals("ArrayFloatType"), "otherType object should be a class of type ArrayfloatType");
            return this.dimension == ((ArrayFloatType) otherType).getDimension();
        }
        return false;
    }
}