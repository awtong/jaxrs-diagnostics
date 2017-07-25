package awt.jaxrs.diagnostics.rules;

import static org.mockito.Mockito.mock;

import javax.ws.rs.container.*;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import awt.jaxrs.diagnostics.mdc.MDCFilter;

public class MDCRule implements TestRule {

    @Override
    public Statement apply(final Statement statement, final Description description) {
	final ContainerRequestContext request = mock(ContainerRequestContext.class);
	final ContainerResponseContext response = mock(ContainerResponseContext.class);

	return new Statement() {
	    private final MDCFilter mdc = new MDCFilter();

	    @Override
	    public void evaluate() throws Throwable {
		this.mdc.filter(request);
		statement.evaluate();
		this.mdc.filter(request, response);
	    }
	};
    }
}
