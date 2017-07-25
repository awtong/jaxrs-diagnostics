package awt.jaxrs.diagnostics.exception.mapper;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.lang.invoke.MethodHandles;
import java.util.Set;

import javax.validation.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.*;

import org.slf4j.*;

import com.google.common.collect.Iterables;

import awt.jaxrs.diagnostics.error.*;
import awt.jaxrs.diagnostics.mdc.MDCFilter;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
	final ErrorMessages errors = new ErrorMessages(MDC.get(MDCFilter.REQUEST_ID));
	final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
	if ((violations != null) && !violations.isEmpty()) {
	    violations.forEach(violation -> {
		final Path path = violation.getPropertyPath();
		final ErrorMessage error = new ErrorMessage(violation.getMessage(),
			path == null ? null : Iterables.getLast(path).getName());
		errors.addError(error);
	    });
	}

	LOGGER.error("Validation Failed. Returning errors: '{}'", errors, exception);
	return Response.status(BAD_REQUEST).entity(errors).build();
    }

}
