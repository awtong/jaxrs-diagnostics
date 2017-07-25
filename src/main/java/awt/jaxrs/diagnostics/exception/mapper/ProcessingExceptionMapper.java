package awt.jaxrs.diagnostics.exception.mapper;

import static javax.ws.rs.core.HttpHeaders.RETRY_AFTER;
import static javax.ws.rs.core.Response.Status.*;

import java.lang.invoke.MethodHandles;
import java.net.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

import org.slf4j.*;

import awt.jaxrs.diagnostics.error.ErrorMessages;
import awt.jaxrs.diagnostics.mdc.MDCFilter;

@Provider
public class ProcessingExceptionMapper implements ExceptionMapper<ProcessingException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String RETRY_IN_SECONDS = "60";

    @Override
    public Response toResponse(final ProcessingException exception) {
	LOGGER.error("Error calling the JAX-RS endpoint.", exception);

	final String logId = MDC.get(MDCFilter.REQUEST_ID);
	final ErrorMessages errors = LOGGER.isInfoEnabled() ? ErrorMessages.fromException(logId, exception)
		: new ErrorMessages(logId);

	final Throwable cause = exception.getCause();
	if ((cause instanceof ConnectException) || (cause instanceof SocketTimeoutException)) {
	    return Response.status(SERVICE_UNAVAILABLE).header(RETRY_AFTER, RETRY_IN_SECONDS).entity(errors).build();
	} else {
	    return Response.status(INTERNAL_SERVER_ERROR).entity(errors).build();
	}
    }
}
