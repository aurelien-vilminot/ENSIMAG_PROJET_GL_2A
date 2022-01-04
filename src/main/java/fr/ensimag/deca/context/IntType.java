package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import org.apache.commons.lang.Validate;

/**
 *
 * @author Aurélien VILMINOT
 * @date 04/01/2022
 */
public class IntType extends Type {

    public IntType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean sameType(Type otherType) {
        Validate.isTrue(otherType != null, "otherType object should not be null");
        return otherType.isInt();
    }


}
