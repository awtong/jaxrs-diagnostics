package awt.jaxrs.diagnostics.exception.mapper;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

import java.lang.invoke.MethodHandles;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

import org.slf4j.*;

import com.typesafe.config.*;

import awt.jaxrs.diagnostics.error.ErrorMessages;

@Provider
public class ThrowableMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Response toResponse(final Throwable throwable) {

	if (throwable instanceof WebApplicationException) {
	    final WebApplicationException exception = (WebApplicationException) throwable;
	    final Response response = exception.getResponse();
	    if (response.hasEntity()) {
		return response;
	    }

	    final ErrorMessages errors = this.getErrorMessages(throwable);
	    return Response.status(response.getStatus()).entity(errors).build();
	}

	LOGGER.error("Handled by the generic exception mapper.", throwable);

	final ErrorMessages errors = this.getErrorMessages(throwable);
	return Response.status(INTERNAL_SERVER_ERROR).entity(errors).build();
    }

    private ErrorMessages getErrorMessages(final Throwable throwable) {
	final Config config = ConfigFactory.load();

	final String logId = MDC.get(config.getString("logging.log-id"));
	final ErrorMessages errors = LOGGER.isInfoEnabled() ? ErrorMessages.fromException(logId, throwable)
		: new ErrorMessages(logId);
	return errors;
    }
}