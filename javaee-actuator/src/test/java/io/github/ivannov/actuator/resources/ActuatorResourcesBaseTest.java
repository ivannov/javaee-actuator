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
