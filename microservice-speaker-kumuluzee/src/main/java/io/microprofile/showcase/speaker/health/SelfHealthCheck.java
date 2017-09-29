package io.microprofile.showcase.speaker.health;

import com.kumuluz.ee.common.config.EeConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class SelfHealthCheck implements HealthCheck {

    @Inject
    @ConfigProperty(name = "health.health-check-http-method", defaultValue = "GET")
    String healthCheckHttpMethod;

    private static final String port = EeConfig.getInstance().getServer().getHttp().getPort() + "";
    private static final String url = "http://localhost:" + port + "/speaker";

    private static final Logger LOG = Logger.getLogger(SelfHealthCheck.class.getSimpleName());

    public HealthCheckResponse call() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(healthCheckHttpMethod);

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.named(SelfHealthCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }
        return HealthCheckResponse.named(SelfHealthCheck.class.getSimpleName()).down().build();
    }
}
