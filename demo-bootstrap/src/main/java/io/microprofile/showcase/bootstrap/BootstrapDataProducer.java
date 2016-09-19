package io.microprofile.showcase.bootstrap;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Heiko Braun
 * @since 15/09/16
 */
@ApplicationScoped
public class BootstrapDataProducer {

    @Produces
    public BootstrapData load() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("schedule.json");
        assert resource !=null : "Failed to load 'schedule.json'";

        Parser parser = new Parser();
        BootstrapData data = parser.parse(resource);

        Logger.getLogger(BootstrapData.class.getName()).log(Level.INFO, "Schedule contains "+data.getSessions().size() + " sessions");

        return data;
    }

}

