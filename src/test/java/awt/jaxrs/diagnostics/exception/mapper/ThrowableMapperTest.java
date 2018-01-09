package awt.jaxrs.diagnostics.exception.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.junit.*;
import org.slf4j.*;

import com.typesafe.config.*;

import awt.jaxrs.diagnostics.error.*;
import awt.jaxrs.diagnostics.rules.MDCRule;

public class ThrowableMapperTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Rule
    public final MDCRule rule = new MDCRule();

    @Test
    public void test() {
	final Config config = ConfigFactory.load();

	final ThrowableMapper mapper = new ThrowableMapper();
	final Response response = mapper.toResponse(new Throwable());
	assertThat(response.getStatus(), is(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()));

	final ErrorMessages errorMessages = (ErrorMessages) response.getEntity();
	assertThat(errorMessages, is(notNullValue()));
	assertThat(errorMessages.getLogId(), is(MDC.get(config.getString("logging.log-id"))));

	final Collection<ErrorMessage> errors = errorMessages.getErrors();
	assertThat(errors.size(), is(LOGGER.isInfoEnabled() ? 1 : 0));
    }
}
