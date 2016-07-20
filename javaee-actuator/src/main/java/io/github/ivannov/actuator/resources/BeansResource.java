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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
    public Response getBeans(@QueryParam("package") String filterPackage) {
        JsonArrayBuilder beansArrayBuilder = Json.createArrayBuilder();
        beansInspector.getBeans().stream()
                                .filter(bean -> letBeanIn(filterPackage, bean))
                                .map(this::jsonifyBean)
                                .forEach(beansArrayBuilder::add);        ;
        JsonObjectBuilder rootJson = Json.createObjectBuilder().add("beans", beansArrayBuilder);
        return Response.ok(rootJson.build()).build();
    }

    private boolean letBeanIn(String filterPackage, Bean<?> bean) {
        return filterPackage == null || bean.getBeanClass().getCanonicalName().startsWith(filterPackage);
    }

    private JsonObjectBuilder jsonifyBean(Bean<?> bean) {
        JsonObjectBuilder beanJson = Json.createObjectBuilder();
        beanJson.add("class", bean.getBeanClass().getCanonicalName());
        beanJson.add("scope", bean.getScope().getSimpleName());
        JsonArrayBuilder dependenciesArrayBuilder = Json.createArrayBuilder();
        bean.getInjectionPoints().forEach(injectionPoint -> dependenciesArrayBuilder.add(injectionPoint.getMember().getName()));
        beanJson.add("dependencies", dependenciesArrayBuilder);
        return beanJson;
    }
}
