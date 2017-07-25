package awt.jaxrs.diagnostics.statistics;

import java.io.IOException;
import java.util.*;

import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class StatisticsFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Collection<Statistics> STATISTICS = new HashSet<>();

    @Context
    private ResourceInfo resourceInfo;

    // in milliseconds
    private long start;

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
	this.start = System.currentTimeMillis();
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
	    throws IOException {
	final long elapsedTime = System.currentTimeMillis() - this.start;
	final String key = this.resourceInfo.getResourceClass().getSimpleName() + "."
		+ this.resourceInfo.getResourceMethod().getName();
	final Statistics statistics = STATISTICS.stream().filter(stat -> key.equals(stat.getKey())).findFirst()
		.orElse(new Statistics(key));
	statistics.increment();
	statistics.addToTotal(elapsedTime);
	STATISTICS.add(statistics);
    }

    public static Collection<Statistics> getStatistics() {
	return STATISTICS;
    }
}
