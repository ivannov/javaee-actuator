/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.JmxInspector;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/metrics")
@RequestScoped
public class MetricsResource {

    @Inject
    private JmxInspector jmxInspector;

	@GET
	@Produces("application/json")
	public Response getResourceUsage() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("cpu", jmxInspector.getProcessCPU());
        Map<String, Long> heapMemory = jmxInspector.getHeapMemory();
        heapMemory.entrySet().forEach(entry -> jsonObjectBuilder.add("heap." + entry.getKey(), entry.getValue()));
        Map<String, Integer> threadInfo = jmxInspector.getThreadDetails();
        threadInfo.entrySet().forEach(entry -> jsonObjectBuilder.add(entry.getKey(), entry.getValue()));
        Map<String, Long> loadedClassesInfo = jmxInspector.getLoadedClassesInfo();
        loadedClassesInfo.entrySet().forEach(entry -> jsonObjectBuilder.add(entry.getKey(), entry.getValue()));
        Map<String, Long> gcInfo = jmxInspector.getGCInfo();
        gcInfo.entrySet().forEach(entry -> jsonObjectBuilder.add("gc." + entry.getKey(), entry.getValue()));
        return Response.ok(jsonObjectBuilder.build()).build();
	}
}