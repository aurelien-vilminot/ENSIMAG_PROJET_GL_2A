package fr.ensimag.deca.tools;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Manual unit-test. We call the functions we want to tests and check that they
 * produce the expected output. The same can be achieved more efficiently with
 * JUnit, see SymbolTest.java for an example
 */
public class ManualTestSymbol {
    /**
     * Raises an exception if the condition is false.
     * 
     * Same as Java's built-in assert(), except it cannot be disabled (Java's
     * assert() are disabled at runtime and need to be enabled using java
     * -enableassertions ...)
     */
    static void assertTrue(boolean condition, String reason) {
        if (!condition) {
            throw new DecacInternalError("Assertion failed: " + reason);
        }
    }

    public static void main(String[] args) {
        SymbolTable t = new SymbolTable();
        Symbol s1 = t.create("foo");
        // Check the output manually with an if statement
        if (!s1.getName().equals("foo")) {
            System.out.println("s1.getName() is equal to \"foo\", it shouldn't.");
            // Notify the failure by exiting with a status != 0.
            System.exit(1);
        }
        Symbol s2 = t.create("foo");
        // Java's assert() does essentially what we did with an if statement
        // above.
        assertTrue(s1 == s2, "Twice the same t.create do not yield twice the same object");
        Symbol s3 = t.create("foobar");
        assertTrue(s1 != s3, "t.create yields the same object for different strings");
        System.out.println("Tests passed successfully.");
    }
}
