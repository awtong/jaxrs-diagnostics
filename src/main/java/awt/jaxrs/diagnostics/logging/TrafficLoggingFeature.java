package awt.jaxrs.diagnostics.logging;

import javax.ws.rs.container.*;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
public class TrafficLoggingFeature implements DynamicFeature {

    @Override
    public void configure(final ResourceInfo resourceInfo, final FeatureContext context) {
	context.register(TrafficLoggingFilter.class);
    }
}
