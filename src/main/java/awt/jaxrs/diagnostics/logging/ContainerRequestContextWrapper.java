package awt.jaxrs.diagnostics.logging;

import java.io.*;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.io.ByteStreams;

public class ContainerRequestContextWrapper {
    private final ContainerRequestContext request;

    public ContainerRequestContextWrapper(final ContainerRequestContext request) {
	this.request = request;
    }

    public String joinHeaders() {
	final HeaderFormatter formatter = new HeaderFormatter(this.request.getHeaders());
	return formatter.join();
    }

    public String getPayloadAsString() throws IOException {
	try (final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final InputStream input = this.request.getEntityStream();) {

	    final StringBuilder builder = new StringBuilder();
	    if (input != null) {
		ByteStreams.copy(input, output);
		final byte[] content = output.toByteArray();
		builder.append(new String(content));

		this.request.setEntityStream(new ByteArrayInputStream(content));
	    }

	    return builder.toString();
	}
    }
}
