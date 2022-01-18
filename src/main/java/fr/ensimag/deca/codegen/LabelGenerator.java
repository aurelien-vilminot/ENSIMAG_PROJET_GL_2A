package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.commons.lang.Validate;

import java.util.HashMap;

public class LabelGenerator {
    private HashMap<String, Integer> labels = new HashMap<>();

    private boolean overflowError = false;
    private boolean stackOverflowError = false;
    private boolean ioError = false;
    private boolean dereferenceError = false;

    public Label getOverFlowLabel() {
        setOverflowError();
        return new Label("overflow_error");
    }

    public boolean getOverflowError() {
        return overflowError;
    }
    public void setOverflowError() {
        this.overflowError = true;
    }

    public Label getStackOverFlowLabel() {
        setStackOverflowError();
        return new Label("stack_overflow_error");
    }

    public boolean getStackOverflowError() {
        return stackOverflowError;
    }
    public void setStackOverflowError() {
        this.stackOverflowError = true;
    }

    public Label getIoLabel() {
        setIoError();
        return new Label("io_error");
    }

    public boolean getIoError() {
        return ioError;
    }
    public void setIoError() {
        this.ioError = true;
    }

    public Label getDereferenceLabel() {
        setDereferenceError();
        return new Label("null_dereference");
    }

    public void setDereferenceError() {
        this.dereferenceError = true;
    }

    public boolean getDereferenceError() {
        return dereferenceError;
    }

    /**
     * Generate unique label based on the typeOfLabel given.
     * For example: generateLabel("begin_while") will result the label "begin_while.1" if it is the first declared.
     * @param typeOfLabel The name of label
     * @return A string which contains the name of label with a unique number
     */
    public String generateLabel(String typeOfLabel) {
        Validate.notNull(typeOfLabel, "The label should not be null element");
        String newLabel;

        if (labels.containsKey(typeOfLabel)) {
            // Existing type of label, increment the number and insert in the map
            int labelNumber = labels.get(typeOfLabel);
            labelNumber++;
            labels.put(typeOfLabel, labelNumber);
            newLabel = typeOfLabel + '.' + labelNumber;
        } else {
            // New type of label, insertion in the map
            labels.put(typeOfLabel, 1);
            newLabel = typeOfLabel + '.' + 1;
        }
        return newLabel;
    }

    public void generateErrorLabel(DecacCompiler compiler, Label label, String message) {
        compiler.addLabel(label);
        compiler.addInstruction(new WSTR(message));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }
}
