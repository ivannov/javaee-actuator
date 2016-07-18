package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.BeansInspector;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * @author Ivan St. Ivanov.
 */
@Path("/beans")
@RequestScoped
public class BeansResource {

    @Inject
    private BeansInspector beansInspector;

    @GET
    @Produces("application/json")
    public Response getBeans() {
        JsonObjectBuilder rootObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder beansArrayBuilder = Json.createArrayBuilder();

        Set<Bean<?>> beans = beansInspector.getBeans();

        for (Bean<?> bean : beans) {
            JsonObjectBuilder beanJson = Json.createObjectBuilder();
            beanJson.add("class", bean.getBeanClass().getCanonicalName());
            beanJson.add("scope", bean.getScope().getSimpleName());
            JsonArrayBuilder dependenciesArrayBuilder = Json.createArrayBuilder();
            bean.getInjectionPoints().forEach(injectionPoint -> dependenciesArrayBuilder.add(injectionPoint.getMember().getName()));
            beanJson.add("dependencies", dependenciesArrayBuilder);
            beansArrayBuilder.add(beanJson);
        }
        rootObjectBuilder.add("beans", beansArrayBuilder);
        return Response.ok(rootObjectBuilder.build()).build();
    }
}
