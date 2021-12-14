package tools;

import org.apache.log4j.Logger;

public class DireBonjour extends SayHello {
    private static final Logger LOG = Logger.getLogger(DireBonjour.class);

    @Override
    public String getMessage() {
        LOG.trace("Appel de getMessage() dans DireBonjour");
        return "Bonjour";
    }

    @Override
    public String getOtherMessage() {
        LOG.trace("Appel de getOtherMessage() dans DireBonjour");
        return "Au revoir";
    }

}
