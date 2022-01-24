package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNew {
    @Test
    public void testNewInst() {
        // Check validate
        assertThrows(IllegalArgumentException.class, () -> new New(null));
    }

    @Test
    public void testNewVerify() throws EnvironmentTypes.DoubleDefException, ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        SymbolTable.Symbol symbol = compiler.getSymbolTable().create("foo");
        compiler.getEnvironmentTypes().declare(
                symbol,
                new ClassDefinition(
                        new ClassType(symbol, null, null),
                        null,
                        null
                )
        );
        Identifier identifier = new Identifier(symbol);
        New aNew = new New(identifier);

        // Check validate
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            aNew.verifyExpr(null, null, null);
        });
        assertEquals("Compiler (env_types) object should not be null", exception.getMessage());

        // Check returned type
        assertTrue(aNew.verifyExpr(compiler, null, null).isClass());

        // Check New with a bad type (int)
        SymbolTable.Symbol intSymbol = compiler.getSymbolTable().create("bar");
        compiler.getEnvironmentTypes().declare(
                intSymbol,
                new TypeDefinition(
                        new IntType(intSymbol),
                        null
                )
        );
        Identifier intIdentifier = new Identifier(intSymbol);
        New intNew = new New(intIdentifier);

        exception = assertThrows(ContextualError.class, () -> {
            intNew.verifyExpr(compiler, null, null);
        });
        assertEquals("Cannot instantiate a class, the type must be a class", exception.getMessage());
    }
}
