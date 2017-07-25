package awt.jaxrs.diagnostics.logging;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

import org.slf4j.*;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class TrafficLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Context
    private Providers providers;

    @Override
    public void filter(final ContainerRequestContext request) throws IOException {
	LOGGER.info("Entering: [{}] {}", request.getMethod(), request.getUriInfo().getAbsolutePath());
	final ContainerRequestContextWrapper wrapper = new ContainerRequestContextWrapper(request);
	final MultivaluedMap<String, String> headers = request.getHeaders();
	if ((headers != null) && !headers.isEmpty()) {
	    LOGGER.debug(" > Request Headers: {}", wrapper.joinHeaders());
	}

	if (request.hasEntity()) {
	    LOGGER.debug(" > Request Payload: {}", wrapper.getPayloadAsString());
	}
    }

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response)
	    throws IOException {
	LOGGER.info("Exiting: [{}] {} with status code {}", request.getMethod(), request.getUriInfo().getAbsolutePath(),
		response.getStatus());
	final ContainerResponseContextWrapper wrapper = new ContainerResponseContextWrapper(response);
	final MultivaluedMap<String, Object> headers = response.getHeaders();
	if ((headers != null) && !headers.isEmpty()) {
	    LOGGER.debug(" > Response Headers: {}", wrapper.joinHeaders());
	}

	if (response.hasEntity()) {
	    LOGGER.debug(" > Response Payload: {}", wrapper.getPayloadAsString(this.providers));
	}
    }
}