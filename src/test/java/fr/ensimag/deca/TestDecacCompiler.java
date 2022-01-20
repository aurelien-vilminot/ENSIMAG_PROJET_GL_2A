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
        assertEquals(10, this.decacCompiler.getTempStackMax());
        this.decacCompiler.setTempStackMax(0);
        assertEquals(0, this.decacCompiler.getTempStackMax());
    }

    @Test
    public void testUsedRegsiter() {
        assertEquals(0, this.decacCompiler.getCurrentRegister());
        assertEquals(0, this.decacCompiler.getNumberOfRegistersUsed());

        this.decacCompiler.setMaxUsedRegister(5);
        assertEquals(4, this.decacCompiler.getNumberOfRegistersUsed());

        assertEquals(15, this.decacCompiler.setAndVerifyCurrentRegister(4));
        assertEquals(4, this.decacCompiler.getCurrentRegister());
        assertEquals(4, this.decacCompiler.getNumberOfRegistersUsed());
    }

}
