package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.jmx.UsageBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

@Path("/metrics")
@RequestScoped
public class Metrics {

    @Inject
    private UsageBean usageBean;

	@GET
	@Produces("application/json")
	public Response getResourceUsage() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("heapMemoryUsage", usageBean.getHeapMemoryUsageInMB());
		jsonObjectBuilder.add("usedProcessCPU", usageBean.getProcessCPU());
		return Response.ok(jsonObjectBuilder.build()).build();
	}
}