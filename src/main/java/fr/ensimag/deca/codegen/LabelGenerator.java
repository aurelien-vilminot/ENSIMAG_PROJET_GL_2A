package fr.ensimag.deca.codegen;

import java.util.HashMap;

public class LabelGenerator {
    private HashMap<String, Integer> labels = new HashMap<>();

    public String generateLabel(String typeOfLabel) {
        String newLabel;

        if (labels.containsKey(typeOfLabel)) {
            int labelNumber = labels.get(typeOfLabel);
            labelNumber++;
            labels.put(typeOfLabel, labelNumber);
            newLabel = typeOfLabel + '.' + labelNumber;
        } else {
            labels.put(typeOfLabel, 1);
            newLabel = typeOfLabel + '.' + 1;
        }
        return newLabel;
    }
}
