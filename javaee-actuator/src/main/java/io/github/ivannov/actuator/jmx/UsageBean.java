package io.github.ivannov.actuator.jmx;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.management.*;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * @author Ivan St. Ivanov.
 */
@ApplicationScoped
public class UsageBean {

    private static final String JMX_HOST = "127.0.0.1";

    private JMXConnector jmxConnector;

    @PostConstruct
    public void initializeJMXConnector() {
        try {
            String managementPort = System.getProperty("com.sun.management.jmxremote.port");
            JMXServiceURL jmxServiceURL = new JMXServiceURL("rmi", "", 0, "/jndi/rmi://" + JMX_HOST + ":" + managementPort + "/jmxrmi");
            jmxConnector = JMXConnectorFactory.newJMXConnector(jmxServiceURL, new HashMap<>());
            jmxConnector.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHeapMemoryUsageInMB() {
        try {
            Object o = jmxConnector.getMBeanServerConnection().getAttribute(new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
            CompositeData cd = (CompositeData) o;
            Long usedInMB = ((Long) cd.get("used")) / 1024 / 1024;
            return usedInMB.toString() + "MB";
        } catch (MBeanException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | IOException | MalformedObjectNameException e) {
            return "Holly smokes";
        }
    }

    @PreDestroy
    public void closeJMXConnector() {
        try {
            if (jmxConnector != null) {
                jmxConnector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
