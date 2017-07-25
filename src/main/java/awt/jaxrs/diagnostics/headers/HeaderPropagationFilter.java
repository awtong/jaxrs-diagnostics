package awt.jaxrs.diagnostics.headers;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

import com.google.common.base.Strings;

import awt.jaxrs.diagnostics.mdc.MDCFilter;

@Provider
public class HeaderPropagationFilter implements ContainerResponseFilter {
    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response)
	    throws IOException {
	final String id = MDC.get(MDCFilter.REQUEST_ID);
	response.getHeaders().putSingle(Header.X_CALL_ID.getName(), id == null ? UUID.randomUUID().toString() : id);

	final String correlationId = request.getHeaderString(Header.X_CORRELATION_ID.getName());
	response.getHeaders().putSingle(Header.X_CORRELATION_ID.getName(),
		Strings.isNullOrEmpty(correlationId) ? UUID.randomUUID().toString() : correlationId);

    }
}
