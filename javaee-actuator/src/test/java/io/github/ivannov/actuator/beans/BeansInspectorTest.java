package io.github.ivannov.actuator.beans;

import io.github.ivannov.actuator.inspectors.BeansInspector;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class BeansInspectorTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(BeansInspector.class);
    }

    @Inject
    private BeansInspector beansInspector;

    @Test
    public void testBeansInspector() throws Exception {
        assertNotNull(beansInspector);
    }
}
