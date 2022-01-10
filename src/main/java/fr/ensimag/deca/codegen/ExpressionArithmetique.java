package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.SUB;

public class ExpressionArithmetique {
    public static Instruction mnemo(String operatorName, DVal op1, GPRegister op2) {
        switch (operatorName) {
            case "+":
                return new ADD(op1, op2);
            case "-":
                return new SUB(op1, op2);
            case "*":
                return new MUL(op1, op2);
            case "/":
                return new DIV(op1, op2);
            default:
                return null;
        }
    }
}
