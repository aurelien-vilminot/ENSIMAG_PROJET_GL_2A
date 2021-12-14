package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.UnaryInstructionImmInt;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class SUBSP extends UnaryInstructionImmInt {

    public SUBSP(ImmediateInteger operand) {
        super(operand);
    }

    public SUBSP(int i) {
        super(i);
    }

}
