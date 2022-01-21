package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Pierre ARVY
 * @date 19/01/2022
 */
public class MatrixFloatType extends Type {

    public MatrixFloatType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isMatrixFloat() {
        return true;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        Validate.notNull(otherType, "otherType object should not be null");
        return otherType.isMatrixFloat();
    }


}
