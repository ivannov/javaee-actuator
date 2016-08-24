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
