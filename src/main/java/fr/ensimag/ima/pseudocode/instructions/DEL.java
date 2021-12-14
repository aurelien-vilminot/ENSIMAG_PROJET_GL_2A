package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

/**
 * @author Ensimag
 * @date 01/01/2022
 */
public class DEL extends UnaryInstructionToReg {

    public DEL(GPRegister op) {
        super(op);
    }

}
