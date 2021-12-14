package fr.ensimag.deca.tools;

import java.io.PrintStream;

/**
 *
 * @author @AUTHOR@
 * @date @DATE@
 */
public class IndentPrintStream {
    private PrintStream stream;
    public IndentPrintStream(PrintStream stream) {
        this.stream = stream;
    }
    private int indent = 0;
    private boolean indented = false;

    private void printIndent() {
        if (indented) {
            return;
        }
        for (int i = 0; i < indent; i++) {
            stream.print("\t");
        }
        indented = true;
    }
    public void print(String s) {
        printIndent();
        stream.print(s);
    }

    public void println() {
        stream.println();
        indented = false;
    }

    public void println(String s) {
        print(s);
        println();
    }

    public void indent() {
        indent++;
    }

    public void unindent() {
        indent--;
    }

    public void print(char charAt) {
        printIndent();
        stream.print(charAt);
    }
}
