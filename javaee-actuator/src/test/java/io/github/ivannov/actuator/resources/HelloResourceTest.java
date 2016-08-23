package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.config.RestConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class HelloResourceTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(HelloResource.class)
                .addClass(RestConfig.class);
    }

    @Test
    public void shouldGetHelloWorld(@ArquillianResource URL base) throws Exception {
        URL url = new URL(base, "actuator/hello");
        WebTarget target = ClientBuilder.newClient().target(url.toExternalForm());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        String jsonString = response.readEntity(String.class);
        StringReader stringReader = new StringReader(jsonString);
        JsonReader jsonReader = Json.createReader(stringReader);
        JsonObject helloWorld = jsonReader.readObject();
        assertEquals("world", helloWorld.getString("hello"));
    }
}
