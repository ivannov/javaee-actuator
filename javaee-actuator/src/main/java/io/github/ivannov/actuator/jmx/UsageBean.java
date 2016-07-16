package io.github.ivannov.actuator.jmx;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
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
            CompositeData cd = (CompositeData) getAttributeValue("java.lang:type=Memory", "HeapMemoryUsage");
            Long usedInMB = ((Long) cd.get("used")) / 1024 / 1024;
            return usedInMB.toString() + " MB";
        } catch (Exception e) {
            return "Holly smokes";
        }
    }

    private Object getAttributeValue(String objectName, String attributeName) throws Exception {
        return jmxConnector.getMBeanServerConnection().getAttribute(new ObjectName(objectName), attributeName);
    }

    public String getProcessCPU() {
        try {
            double cpuUsage = (double) getAttributeValue("java.lang:type=OperatingSystem", "ProcessCpuLoad");
            return ((int) (cpuUsage * 100)) + " %";
        } catch (Exception e) {
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
