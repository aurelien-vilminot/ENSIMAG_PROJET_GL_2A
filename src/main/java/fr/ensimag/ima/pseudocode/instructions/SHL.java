package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

/**
 * @author Ensimag
 * @date 01/01/2022
 */
public class SHL extends UnaryInstructionToReg {
    public SHL(GPRegister op1) {
        super(op1);
    }
}
