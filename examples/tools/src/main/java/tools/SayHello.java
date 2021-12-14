package tools;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author Ensimag
 * @date @DATE@
 */
class SayHello {
    private static final Logger LOG = Logger.getLogger(SayHello.class);

    public SayHello() {
    }

    public String getMessage() {
        return "Hello, world";
    }

    public String getOtherMessage() {
        return "Good bye";
    }

    public String getBothMessages() {
        return getMessage() + "\n" + getOtherMessage();
    }

    public void sayIt() {
        LOG.debug("I'm going to say it");
        System.out.println(getMessage());
    }

    public void sayOtherMessage() {
        LOG.trace("I'm going to say the other thing");
        System.out.println(getOtherMessage());
    }

    public void sayItTo(String name) {
        Validate.notNull(name);
        System.out.println("Hello, " + name);
    }

}
