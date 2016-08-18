package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.config.RestConfig;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ivan St. Ivanov.
 */
abstract class ActuatorResourcesBaseTest {

    private @ArquillianResource URL base;

    static WebArchive initDeployment() {
        return ShrinkWrap.create(WebArchive.class, "actuator-test.war")
                .addClass(RestConfig.class);
    }

    Response sendRequest(String endpoint) throws MalformedURLException {
        URL url = new URL(base, "actuator/" + endpoint);
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        return target.request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    JsonObject readJsonContent(Response response) {
        String jsonString = response.readEntity(String.class);
        StringReader stringReader = new StringReader(jsonString);
        JsonReader jsonReader = Json.createReader(stringReader);
        return jsonReader.readObject();
    }
}
