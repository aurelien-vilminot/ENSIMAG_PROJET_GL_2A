package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDAddrToReg;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * @author Ensimag
 * @date 01/01/2022
 */
public class LEA extends BinaryInstructionDAddrToReg {

    public LEA(DAddr op1, GPRegister op2) {
        super(op1, op2);
    }

}
