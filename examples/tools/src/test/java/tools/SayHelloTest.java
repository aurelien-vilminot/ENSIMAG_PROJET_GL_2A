package tools;

import static org.junit.Assert.*;
import org.junit.Test;

public class SayHelloTest {
    @Test
    public void testMessage() {
        SayHello sayHello = new SayHello();
        assertEquals("The message should be equal to \"Hello, word\"",
                "Hello, world", sayHello.getMessage());
        assertEquals("The other message should be equal to \"Good bye\"",
                "Good bye", sayHello.getOtherMessage());
    }

    @Test
    public void testBothMessages() {
        SayHello sayHello = new SayHello();
        assertEquals("The message should be equal to \"Hello, word\"",
                "Hello, world\nGood bye", sayHello.getBothMessages());
    }

}
