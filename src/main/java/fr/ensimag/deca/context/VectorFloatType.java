package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl07
 * @date 19/01/2022
 */
public class VectorFloatType extends Type {

    public VectorFloatType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isVectorFloat() {
        return true;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        Validate.notNull(otherType, "otherType object should not be null");
        return otherType.isVectorFloat();
    }


}
