package fr.ensimag.deca.context;

import static org.junit.jupiter.api.Assertions.*;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestType {
    private static SymbolTable.Symbol symbol1;
    private static SymbolTable.Symbol symbol2;
    private static SymbolTable.Symbol symbol3;

    @BeforeAll
    public static void setup() {
        SymbolTable table = new SymbolTable();
        symbol1 = table.create("foo");
        symbol2 = table.create("foo2");
        symbol3 = table.create("foo3");
    }

    @Test
    public void testBooleanType() {

        BooleanType bool1 = new BooleanType(symbol1);
        assertTrue(bool1.isBoolean());

        BooleanType bool2 = new BooleanType(symbol2);
        assertTrue(bool2.sameType(bool1));

        assertNotSame(bool1, bool2);
    }

    @Test
    public void testFloatType() {

        FloatType float1 = new FloatType(symbol1);
        assertTrue(float1.isFloat());

        FloatType float2 = new FloatType(symbol2);
        assertTrue(float1.sameType(float2));
        assertNotSame(float1, float2);

        BooleanType bool = new BooleanType(symbol1);
        assertFalse(float1.sameType(bool));
    }

    @Test
    public void testIntType() {

        IntType int1 = new IntType(symbol1);
        assertTrue(int1.isInt());

        IntType int2 = new IntType(symbol2);
        assertTrue(int1.sameType(int2));
        assertNotSame(int1, int2);

        BooleanType bool = new BooleanType(symbol1);
        assertFalse(int1.sameType(bool));

    }

    @Test
    public void testStringType() {

        StringType string1 = new StringType(symbol1);
        assertTrue(string1.isString());

        StringType string2 = new StringType(symbol2);
        assertTrue(string1.sameType(string2));
        assertNotSame(string1, string2);

        BooleanType bool = new BooleanType(symbol1);
        assertFalse(string1.sameType(bool));

    }

    @Test
    public void testVoidType() {

        VoidType void1 = new VoidType(symbol1);
        assertTrue(void1.isVoid());

        VoidType void2 = new VoidType(symbol2);
        assertTrue(void1.sameType(void2));
        assertNotSame(void1, void2);

        BooleanType bool = new BooleanType(symbol1);
        assertFalse(void1.sameType(bool));

    }

    @Test
    public void testNullType() {

        NullType null1 = new NullType(symbol1);
        assertTrue(null1.isNull());
        assertTrue(null1.isClassOrNull());

        NullType null2 = new NullType(symbol2);
        assertTrue(null1.sameType(null2));
        assertNotSame(null1, null2);

        BooleanType bool = new BooleanType(symbol1);
        assertFalse(null1.sameType(bool));

    }

    @Test
    public void testClassType() {
        ClassType class1 = new ClassType(symbol1, Location.BUILTIN, null);
        assertTrue(class1.isClass());
        assertTrue(class1.isClassOrNull());

        ClassType class2 = new ClassType(symbol2, Location.BUILTIN, null);
        assertTrue(class1.sameType(class2));
        assertNotSame(class1, class2);

        BooleanType bool = new BooleanType(symbol1);
        assertFalse(class1.sameType(bool));

        ClassType class3 = new ClassType(symbol3, Location.BUILTIN, class1.getDefinition());
        assertTrue(class3.isSubClassOf(class1));
        assertFalse(class3.isSubClassOf(class2));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            class3.isSubClassOf(null);
        });
        String expectedMessage = "The potential superclass should not be null";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        assertEquals(class1, class1);
        assertNotEquals(class1, class2);
        assertNotEquals(class1, null);

        assertSame(class1, class1.asClassType(null, null));

        assertInstanceOf(Integer.class, class1.hashCode());
        assertInstanceOf(ClassType.class, class1.asClassType(null, null));
    }
}
