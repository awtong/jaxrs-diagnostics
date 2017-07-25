package awt.jaxrs.diagnostics.headers;

import javax.ws.rs.container.*;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import awt.jaxrs.diagnostics.mdc.MDCFilter;

@Provider
public class HeaderPropagationFeature implements DynamicFeature {

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {
	context.register(MDCFilter.class);
	context.register(HeaderPropagationFilter.class);
    }
}
