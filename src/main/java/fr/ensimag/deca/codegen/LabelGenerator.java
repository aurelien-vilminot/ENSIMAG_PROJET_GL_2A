package fr.ensimag.deca.codegen;

import java.util.HashMap;

public class LabelGenerator {
    private HashMap<String, Integer> labels = new HashMap<>();

    /**
     * Generate unique label based on the typeOfLabel given.
     * For example: generateLabel("begin_while") will result the label "begin_while.1" if it is the first declared.
     * @param typeOfLabel The name of label
     * @return A string which contains the name of label with a unique number
     */
    public String generateLabel(String typeOfLabel) {
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
}
