package awt.jaxrs.diagnostics.mdc;

import java.util.UUID;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

import com.typesafe.config.*;

@Provider
@Priority(1)
public class MDCFilter implements ContainerRequestFilter, ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext request) {
	final UUID uuid = UUID.randomUUID();

	final Config config = ConfigFactory.load();
	MDC.put(config.getString("logging.log-id"), uuid.toString());
    }

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response) {
	final Config config = ConfigFactory.load();
	MDC.remove(config.getString("logging.log-id"));
    }
}
