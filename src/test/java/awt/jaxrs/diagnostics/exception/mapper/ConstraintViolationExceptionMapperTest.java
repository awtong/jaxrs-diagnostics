package awt.jaxrs.diagnostics.exception.mapper;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.*;

import javax.validation.*;
import javax.ws.rs.core.Response;

import org.junit.*;

import awt.jaxrs.diagnostics.error.*;
import awt.jaxrs.diagnostics.rules.MDCRule;

public class ConstraintViolationExceptionMapperTest {

    @Rule
    public final MDCRule rule = new MDCRule();

    /**
     * Tests to make sure no errors are returned when <code>Set</code> of
     * ConstraintViolation</code> is <code>null<code>
     */
    @Test
    public void testNullViolations() {
	final ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();
	final Response response = mapper.toResponse(new ConstraintViolationException(null));
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

	final ErrorMessages errors = (ErrorMessages) response.getEntity();
	assertThat(errors, is(notNullValue()));
	assertThat(errors.getErrors().isEmpty(), is(true));
    }

    /**
     * Tests to make sure no errors are returned when <code>Set</code> of
     * ConstraintViolation</code> is empty.
     */
    @Test
    public void testEmptyViolations() {
	final ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();
	final Response response = mapper.toResponse(new ConstraintViolationException(Collections.emptySet()));
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

	final ErrorMessages errors = (ErrorMessages) response.getEntity();
	assertThat(errors, is(notNullValue()));
	assertThat(errors.getErrors().isEmpty(), is(true));
    }

    /**
     * Tests to make sure errors are returned when <code>Set</code> of
     * ConstraintViolation</code> has elements.
     */
    @Test
    public void testViolations() {
	final ConstraintViolation<?> violation1 = this.getMockConstraintViolation("PathName1", "Message1");
	final ConstraintViolation<?> violation2 = this.getMockConstraintViolation("PathName2", "Message2");

	final Set<ConstraintViolation<?>> violations = new HashSet<>();
	violations.add(violation1);
	violations.add(violation2);

	final ConstraintViolationExceptionMapper mapper = new ConstraintViolationExceptionMapper();
	final Response response = mapper.toResponse(new ConstraintViolationException(violations));
	assertThat(response, is(notNullValue()));
	assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

	final ErrorMessages errorMessages = (ErrorMessages) response.getEntity();
	assertThat(errorMessages, is(notNullValue()));

	final Collection<ErrorMessage> errors = errorMessages.getErrors();
	assertThat(errors.isEmpty(), is(false));
	assertThat(errors.size(), is(2));
    }

    private ConstraintViolation<?> getMockConstraintViolation(final String nodeName, final String message) {
	final ConstraintViolation<?> violation = mock(ConstraintViolation.class);
	final Path path = mock(Path.class);
	final Path.Node node = mock(Path.Node.class);
	when(node.getName()).thenReturn(nodeName);

	@SuppressWarnings("unchecked")
	final Iterator<Path.Node> iterator = mock(Iterator.class);
	when(iterator.next()).thenReturn(node);
	when(path.iterator()).thenReturn(iterator);
	when(violation.getPropertyPath()).thenReturn(path);
	when(violation.getMessage()).thenReturn(message);

	return violation;
    }
}
