package awt.jaxrs.diagnostics.healthcheck;

import static javax.ws.rs.core.MediaType.*;

import java.util.ServiceLoader;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.*;

@Api(value = "health")
@Path("health/")
public class HealthCheckResource {
    private static final HealthRegistry HEALTHY = new HealthRegistry();

    /**
     * Performs a status check to verify server is up.
     *
     * @return HTTP response object containing 200 status code with no payload.
     */
    @ApiOperation(value = "Checks to make sure server can be reached")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Server reached successfully.") })
    @GET
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response getHeartbeat() {
	return Response.ok(HEALTHY).build();
    }

    /**
     * Performs a status check on all internal services (ex: database) are up.
     *
     * @return HTTP response object containing 200 status code with detailed health
     *         check status.
     */
    @ApiOperation(value = "Checks to make sure application services can be reached")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "All services reached successfully."),
	    @ApiResponse(code = 503, message = "Not all services reached successfully.") })
    @GET
    @Path("detailed/")
    @Produces({ APPLICATION_JSON, APPLICATION_XML })
    public Response getDetailedHealthStatus() {
	final HealthRegistry registry = new HealthRegistry();
	for (final HealthStatusProvider check : ServiceLoader.load(HealthStatusProvider.class)) {
	    registry.addStatus(check.getHealthStatus());
	}

	final Status status = registry.isSuccessful() ? Response.Status.OK : Response.Status.SERVICE_UNAVAILABLE;
	return Response.status(status).entity(registry).build();
    }
}
