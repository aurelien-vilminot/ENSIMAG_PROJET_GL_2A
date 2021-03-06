package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestLabelGenerator {
    private LabelGenerator labelGenerator;

    @BeforeEach
    public void setup() {
        this.labelGenerator = new LabelGenerator();
    }

    @Test
    public void testLabelGeneration() {
        // Check validate
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.labelGenerator.generateLabel(null);
        });
        String expectedMessage = "The label should not be null element";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);

        // Check successive label generations
        int labelTemplateNumber = 1;
        String labelTemplate = "template";
        String labelTemplateWithPoint = labelTemplate + '.';
        assertEquals(labelTemplate, this.labelGenerator.generateLabel(labelTemplate));
        assertEquals(labelTemplateWithPoint + ++labelTemplateNumber, this.labelGenerator.generateLabel(labelTemplate));
        assertEquals(labelTemplateWithPoint + ++labelTemplateNumber, this.labelGenerator.generateLabel(labelTemplate));

        // Check new template label generation and multiple labels generations
        int labelTemplate2Number = 1;
        String labelTemplate2 = "foo";
        String labelTemplate2WithPoint = labelTemplate2 + '.';
        assertEquals(labelTemplate2, this.labelGenerator.generateLabel(labelTemplate2));
        assertEquals(labelTemplateWithPoint + ++labelTemplateNumber, this.labelGenerator.generateLabel(labelTemplate));
        assertEquals(labelTemplate2WithPoint + ++labelTemplate2Number, this.labelGenerator.generateLabel(labelTemplate2));
        assertEquals(labelTemplateWithPoint + ++labelTemplateNumber, this.labelGenerator.generateLabel(labelTemplate));

    }

    @Test
    public void testIOError() {
        Label expectedLabel = new Label("io_error");

        // Check if IOError is false by default
        assertFalse(this.labelGenerator.getIoError());

        // Check label getter
        assertEquals(expectedLabel.toString(), this.labelGenerator.getIoLabel().toString());

        // Check implicit set IOError to true
        assertTrue(this.labelGenerator.getIoError());
    }
}
