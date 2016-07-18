package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.BeansInspector;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

/**
 * @author Ivan St. Ivanov.
 */
@Path("/beans")
@RequestScoped
public class BeansResource {

    private static final String LS = System.getProperty("line.separator");

    @Inject
    private BeansInspector beansInspector;

    @GET
    @Produces("text/plain")
    public String getBeans() {
        Set<Bean<?>> beans = beansInspector.getBeans();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Bean<?> bean : beans) {
            sb.append("==== Info about bean #" + (++i) + "====" + LS);
            sb.append("class: " + bean.getBeanClass() + LS);
            sb.append("name: " + bean.getName() + LS);
            sb.append("scope: " + bean.getScope().getName() + LS);
            sb.append("qualifiers: " + bean.getQualifiers() + LS);
            sb.append("types" + bean.getTypes() + LS);
            sb.append("injectionPoints" + bean.getInjectionPoints() + LS);
            sb.append("stereotypes" + bean.getStereotypes() + LS);
            sb.append("==== End info about bean #" + i + "====" + LS + LS);
        }
        return sb.toString();
    }
}
