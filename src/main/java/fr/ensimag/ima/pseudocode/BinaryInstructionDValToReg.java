package fr.ensimag.ima.pseudocode;

/**
 * Base class for instructions with 2 operands, the first being a
 * DVal, and the second a Register.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class BinaryInstructionDValToReg extends BinaryInstruction {
    public BinaryInstructionDValToReg(DVal op1, GPRegister op2) {
        super(op1, op2);
    }
}
