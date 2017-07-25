package awt.jaxrs.diagnostics.exception.mapper;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.lang.invoke.MethodHandles;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

import org.slf4j.*;

import awt.jaxrs.diagnostics.error.ErrorMessages;
import awt.jaxrs.diagnostics.mdc.MDCFilter;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Response toResponse(final Throwable throwable) {
	LOGGER.error("Handled by the generic exception mapper.", throwable);

	final String logId = MDC.get(MDCFilter.REQUEST_ID);
	final ErrorMessages errors = LOGGER.isInfoEnabled() ? ErrorMessages.fromException(logId, throwable)
		: new ErrorMessages(logId);

	return Response.status(INTERNAL_SERVER_ERROR).entity(errors).build();
    }
}