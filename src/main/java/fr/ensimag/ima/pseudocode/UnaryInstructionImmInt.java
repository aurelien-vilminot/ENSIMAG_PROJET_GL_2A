package fr.ensimag.ima.pseudocode;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public abstract class UnaryInstructionImmInt extends UnaryInstruction {

    protected UnaryInstructionImmInt(ImmediateInteger operand) {
        super(operand);
    }

    protected UnaryInstructionImmInt(int i) {
        super(new ImmediateInteger(i));
    }

}
