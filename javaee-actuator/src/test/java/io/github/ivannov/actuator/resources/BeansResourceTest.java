package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.BeansInspector;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Ivan St. Ivanov.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeansResourceTest extends ActuatorResourcesBaseTest {

    @Deployment
    public static WebArchive createDeployment() {
        return initDeployment()
                .addClasses(BeansInspector.class, BeansResource.class);
    }

    @Test
    public void beansResourceShouldHaveProperStructure() throws Exception {
        Response response = sendRequest("beans");
        assertNotNull(response.getEntity());
        JsonArray beansArray = readJsonContent(response).getJsonArray("beans");
        assertNotNull(beansArray);
        JsonValue beansResourceValue = assertBeansResourceBeanPresent(beansArray);
        assertBeansResourceContent(beansResourceValue);
    }

    private JsonValue assertBeansResourceBeanPresent(JsonArray beansArray) {
        Optional<JsonValue> beansResourceValue = beansArray
                                                .stream()
                                                .filter(value -> ((JsonObject) value).getString("class").contains("BeansResource"))
                                                .findAny();
        assertTrue(beansResourceValue.isPresent());
        return beansResourceValue.get();
    }

    private void assertBeansResourceContent(JsonValue beansResourceValue) {
        JsonObject beansResourceObject = (JsonObject) beansResourceValue;
        assertEquals("RequestScoped", beansResourceObject.getString("scope"));
        assertEquals("beansInspector", beansResourceObject.getJsonArray("dependencies").getString(0));
    }

}