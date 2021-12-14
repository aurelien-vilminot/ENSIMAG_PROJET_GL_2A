package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Instruction with a single operand.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public abstract class UnaryInstruction extends Instruction {
    private Operand operand;

    @Override
    void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(operand);
    }

    protected UnaryInstruction(Operand operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    public Operand getOperand() {
        return operand;
    }

}
