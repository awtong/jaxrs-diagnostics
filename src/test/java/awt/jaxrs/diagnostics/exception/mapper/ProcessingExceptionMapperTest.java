package awt.jaxrs.diagnostics.exception.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.Collection;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.*;

import org.junit.*;
import org.slf4j.*;

import com.typesafe.config.*;

import awt.jaxrs.diagnostics.error.*;
import awt.jaxrs.diagnostics.rules.MDCRule;

public class ProcessingExceptionMapperTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Rule
    public final MDCRule rule = new MDCRule();

    @Test
    public void testConnectException() {
	final Config config = ConfigFactory.load();

	final ProcessingException exception = new ProcessingException(new ConnectException());
	final ProcessingExceptionMapper mapper = new ProcessingExceptionMapper();
	final Response response = mapper.toResponse(exception);
	assertThat(response.getStatus(), is(Response.Status.SERVICE_UNAVAILABLE.getStatusCode()));

	final String retryAfter = response.getHeaderString(HttpHeaders.RETRY_AFTER);

	assertThat(retryAfter, is(notNullValue()));

	final ErrorMessages errorMessages = (ErrorMessages) response.getEntity();
	assertThat(errorMessages, is(notNullValue()));
	assertThat(errorMessages.getLogId(), is(MDC.get(config.getString("logging.log-id"))));

	final Collection<ErrorMessage> errors = errorMessages.getErrors();
	assertThat(errors.size(), is(LOGGER.isInfoEnabled() ? 1 : 0));

	LOGGER.error("{}", errorMessages);
    }

    @Test
    public void testSocketTimeoutException() {
	final Config config = ConfigFactory.load();

	final ProcessingException exception = new ProcessingException(new SocketTimeoutException());
	final ProcessingExceptionMapper mapper = new ProcessingExceptionMapper();
	final Response response = mapper.toResponse(exception);
	assertThat(response.getStatus(), is(Response.Status.SERVICE_UNAVAILABLE.getStatusCode()));

	final String retryAfter = response.getHeaderString(HttpHeaders.RETRY_AFTER);
	assertThat(retryAfter, is(notNullValue()));

	final ErrorMessages errorMessages = (ErrorMessages) response.getEntity();
	assertThat(errorMessages, is(notNullValue()));
	assertThat(errorMessages.getLogId(), is(MDC.get(config.getString("logging.log-id"))));

	final Collection<ErrorMessage> errors = errorMessages.getErrors();
	assertThat(errors.size(), is(LOGGER.isInfoEnabled() ? 1 : 0));

	LOGGER.error("{}", errorMessages);
    }

    @Test
    public void testException() {
	final Config config = ConfigFactory.load();

	final ProcessingException exception = new ProcessingException(new Exception("Some Message"));
	final ProcessingExceptionMapper mapper = new ProcessingExceptionMapper();
	final Response response = mapper.toResponse(exception);
	assertThat(response.getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

	final String retryAfter = response.getHeaderString(HttpHeaders.RETRY_AFTER);
	assertThat(retryAfter, is(nullValue()));

	final ErrorMessages errorMessages = (ErrorMessages) response.getEntity();
	assertThat(errorMessages, is(notNullValue()));
	assertThat(errorMessages.getLogId(), is(MDC.get(config.getString("logging.log-id"))));

	final Collection<ErrorMessage> errors = errorMessages.getErrors();
	assertThat(errors.size(), is(LOGGER.isInfoEnabled() ? 1 : 0));

	LOGGER.error("{}", errorMessages);
    }
}
