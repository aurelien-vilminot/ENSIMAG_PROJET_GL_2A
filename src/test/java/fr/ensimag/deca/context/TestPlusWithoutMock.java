package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Plus;
import fr.ensimag.deca.tree.TreeFunction;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the Plus node in a manual way. The same test would be much easier to
 * write using a mock-up framework like Mockito.
 *
 * @see TestPlusPlain to see how the Mockito library can help writing this kind
 * of tests.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class TestPlusWithoutMock {
    static final Type INT = new IntType(null);
    static final Type FLOAT = new FloatType(null);

    /**
     * Stub usable as a replacement for a real class deriving from AbstractExpr.
     *
     * This would typically be much simpler using Mockito.
     */
    static class DummyIntExpression extends AbstractExpr {
        boolean hasBeenVerified = false;

        @Override
        public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                ClassDefinition currentClass) throws ContextualError {
            hasBeenVerified = true;
            return INT;
        }

        @Override
        public void decompile(IndentPrintStream s) {
            throw new UnsupportedOperationException("Should not be called.");
        }

        @Override
        protected void prettyPrintChildren(PrintStream s, String prefix) {
            throw new UnsupportedOperationException("Should not be called.");
        }

        @Override
        protected void iterChildren(TreeFunction f) {
            throw new UnsupportedOperationException("Should not be called.");
        }

        /**
         * Check that the object has been properly used after the test.
         */
        public void checkProperUse() {
            assertTrue(hasBeenVerified, "verifyExpr has not been called");
        }
    }

    @Test
    public void testType() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        DummyIntExpression left = new DummyIntExpression();
        DummyIntExpression right = new DummyIntExpression();
        Plus t = new Plus(left, right);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
        // check that the dummy expression have been called properly.
        left.checkProperUse();
        right.checkProperUse();
    }
}