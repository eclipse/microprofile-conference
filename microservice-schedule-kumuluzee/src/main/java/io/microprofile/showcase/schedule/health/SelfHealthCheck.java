package io.microprofile.showcase.schedule.health;

import com.kumuluz.ee.common.config.EeConfig;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class SelfHealthCheck implements HealthCheck {

    private static final String port = EeConfig.getInstance().getServer().getHttp().getPort() + "";
    private static final String url = "http://localhost:" + port + "/schedule/all";

    private static final Logger LOG = Logger.getLogger(SelfHealthCheck.class.getSimpleName());

    public HealthCheckResponse call() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");

            if (connection.getResponseCode() == 200) {
                return HealthCheckResponse.named(SelfHealthCheck.class.getSimpleName()).up().build();
            }
        } catch (Exception exception) {
            LOG.severe(exception.getMessage());
        }
        return HealthCheckResponse.named(SelfHealthCheck.class.getSimpleName()).down().build();
    }
}
