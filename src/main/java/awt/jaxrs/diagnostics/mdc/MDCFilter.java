package awt.jaxrs.diagnostics.mdc;

import java.util.UUID;

import javax.annotation.Priority;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

@Provider
@Priority(1)
public class MDCFilter implements ContainerRequestFilter, ContainerResponseFilter {
    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String REQUEST_ID = "log-id";

    @Override
    public void filter(final ContainerRequestContext request) {
	final UUID uuid = UUID.randomUUID();
	MDC.put(REQUEST_ID, uuid.toString());
    }

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response) {
	MDC.remove(REQUEST_ID);
    }
}
