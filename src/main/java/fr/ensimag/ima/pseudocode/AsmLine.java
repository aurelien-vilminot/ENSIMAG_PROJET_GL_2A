package fr.ensimag.ima.pseudocode;

import fr.ensimag.ima.pseudocode.AbstractLine;

import java.io.PrintStream;

public class AsmLine extends AbstractLine {

    private String asm;

    public AsmLine(String asm) {
        this.asm = asm;
    }

    @Override
    void display(PrintStream s) {
        s.print(asm);
        s.println();
    }
}
