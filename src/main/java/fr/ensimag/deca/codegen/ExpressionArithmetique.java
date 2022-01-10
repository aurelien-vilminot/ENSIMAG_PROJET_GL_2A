package fr.ensimag.deca.codegen;


public class ExpressionArithmetique {
    public static String mnemo(String operatorName) {
        switch (operatorName) {
            case "+":
                return "ADD";
            case "-":
                return "SUB";
            case "*":
                return "MUL";
            case "/":
                return "DIV";
            default:
                return null;
        }
    }
}
