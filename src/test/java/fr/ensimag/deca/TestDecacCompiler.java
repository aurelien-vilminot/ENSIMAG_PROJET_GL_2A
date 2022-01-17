package fr.ensimag.deca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDecacCompiler {
    private DecacCompiler decacCompiler;

    @BeforeEach
    public void setup() {
        CompilerOptions compilerOptions = new CompilerOptions();
        this.decacCompiler = new DecacCompiler(compilerOptions, null);
    }

    @Test
    public void testStackSize() {
        // Global stack size
        assertEquals(0, this.decacCompiler.incGlobalStackSize(0));
        assertEquals(10, this.decacCompiler.incGlobalStackSize(10));

        // Temporary stack usage
        assertEquals(0, this.decacCompiler.incTempStackCurrent(0));
        assertEquals(10, this.decacCompiler.incTempStackCurrent(10));

        // Stack max usage
        assertEquals(0, this.decacCompiler.getTempStackMax());
        this.decacCompiler.setTempStackMax();
        assertEquals(10, this.decacCompiler.getTempStackMax());
    }

}
