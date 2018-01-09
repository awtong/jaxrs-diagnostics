package awt.jaxrs.diagnostics.headers;

import java.io.IOException;
import java.util.*;

import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;

import org.slf4j.MDC;

import com.google.common.base.Strings;
import com.typesafe.config.*;

@Provider
public class HeaderPropagationFilter implements ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext request, final ContainerResponseContext response)
	    throws IOException {
	final Config config = ConfigFactory.load();
	final String id = MDC.get(config.getString("logging.log-id"));
	response.getHeaders().putSingle(config.getString("httpheader.request-id"),
		Strings.isNullOrEmpty(id) ? UUID.randomUUID().toString() : id);

	final String correlationId = request.getHeaderString(config.getString("httpheader.correlation-id"));
	response.getHeaders().putSingle(config.getString("httpheader.correlation-id"),
		Strings.isNullOrEmpty(correlationId) ? UUID.randomUUID().toString() : correlationId);

	if (config.hasPath("httpheader.extras")) {
	    final List<String> extraHeaders = config.getStringList("httpheader.extras");
	    for (final String extraHeader : extraHeaders) {
		final String extraHeaderValue = request.getHeaderString(extraHeader);
		if (!Strings.isNullOrEmpty(extraHeaderValue)) {
		    response.getHeaders().putSingle(extraHeader, extraHeaderValue);
		}
	    }
	}
    }
}
