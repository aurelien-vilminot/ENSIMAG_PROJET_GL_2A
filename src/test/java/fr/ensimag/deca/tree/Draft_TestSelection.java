package fr.ensimag.deca.tree;

import static org.junit.jupiter.api.Assertions.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

public class Draft_TestSelection {
    DecacCompiler compiler;
    EnvironmentExp localEnv;
    Selection selection;

    @Mock AbstractExpr expr;
    @Mock AbstractIdentifier ident;
    @Mock ClassDefinition currentClass;

    //@BeforeEach
    void setup() {
        compiler = new DecacCompiler(null, null);
        localEnv = new EnvironmentExp(null);
        selection = new Selection(expr, ident);
    }

    //@Test
    public void testVerifyExprUndefinedClass() {

    }

    //@Test
    public void testVerifyExprPublic() {

    }

    //@Test
    public void testVerifyExprProtected() {

    }
}
