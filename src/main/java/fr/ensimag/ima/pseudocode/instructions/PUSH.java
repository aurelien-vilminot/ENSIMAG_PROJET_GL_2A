package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

/**
 * @author Ensimag
 * @date 01/01/2022
 */
public class PUSH extends UnaryInstruction {
    public PUSH(Register op1) {
        super(op1);
    }
}
