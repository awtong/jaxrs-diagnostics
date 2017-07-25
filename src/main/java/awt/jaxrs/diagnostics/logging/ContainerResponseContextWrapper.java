package awt.jaxrs.diagnostics.logging;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

import org.slf4j.*;

public class ContainerResponseContextWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ContainerResponseContext response;

    public ContainerResponseContextWrapper(final ContainerResponseContext response) {
	this.response = response;
    }

    public String joinHeaders() {
	final HeaderFormatter formatter = new HeaderFormatter(this.response.getHeaders());
	return formatter.join();
    }

    public String getPayloadAsString(final Providers providers) throws IOException {
	final Object entity = this.response.getEntity();
	final MediaType type = this.response.getMediaType();

	final Class<?> clazz = entity.getClass();

	@SuppressWarnings("unchecked")
	final MessageBodyWriter<Object> mbw = (MessageBodyWriter<Object>) providers.getMessageBodyWriter(clazz,
		clazz, new Annotation[] {}, type);
	try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	    if (mbw != null) {
		mbw.writeTo(entity, clazz, clazz, new Annotation[] {}, type, new MultivaluedHashMap<String, Object>(),
			baos);
	    }

	    return baos.toString();
	} catch (final Throwable throwable) {
	    LOGGER.warn("Cannot convert entity. Using default message.", throwable);
	    return "Payload unavailable";
	}
    }
}
