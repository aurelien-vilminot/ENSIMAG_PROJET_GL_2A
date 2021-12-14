package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a variable.
 *
 * @author gl07
 * @date 01/01/2022
 */
public class VariableDefinition extends ExpDefinition {
    public VariableDefinition(Type type, Location location) {
        super(type, location);
    }

    @Override
    public String getNature() {
        return "variable";
    }

    @Override
    public boolean isExpression() {
        return true;
    }
}
