package awt.jaxrs.diagnostics.statistics;

import static javax.ws.rs.core.MediaType.*;

import java.util.Collection;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("statistics/")
public class StatisticsResource {

    @DELETE
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response clearStatistics() {
	StatisticsFilter.getStatistics().clear();
	return Response.noContent().build();
    }

    @GET
    @Consumes({ APPLICATION_JSON, APPLICATION_XML })
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Collection<Statistics> getStatistics() {
	return StatisticsFilter.getStatistics();
    }
}
