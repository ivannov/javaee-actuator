package io.github.ivannov.actuator.inspectors;

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
import java.util.Map;

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

    public int getProcessCPU() {
        try {
            double cpuUsage = (double) getAttributeValue("java.lang:type=OperatingSystem", "ProcessCpuLoad");
            return (int) (cpuUsage * 100);
        } catch (Exception e) {
            return -1;
        }
    }

    public Map<String, Long> getHeapMemory() {
        Map<String, Long> heapMemory = new HashMap<>();
        try {
            CompositeData cd = (CompositeData) getAttributeValue("java.lang:type=Memory", "HeapMemoryUsage");
            heapMemory.put("used", ((Long) cd.get("used")) / 1024);
            heapMemory.put("committed", ((Long) cd.get("committed")) / 1024);
            heapMemory.put("init", ((Long) cd.get("init")) / 1024);
            heapMemory.put("max", ((Long) cd.get("max")) / 1024);
            return heapMemory;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return heapMemory;
    }

    public Map<String, Integer> getThreadsInfo() {
        Map<String, Integer> threadsInfo = new HashMap<>();
        try {
            threadsInfo.put("threads", (Integer) getAttributeValue("java.lang:type=Threading", "ThreadCount"));
            threadsInfo.put("threads.peak", (Integer) getAttributeValue("java.lang:type=Threading", "PeakThreadCount"));
            threadsInfo.put("threads.daemon", (Integer) getAttributeValue("java.lang:type=Threading", "DaemonThreadCount"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return threadsInfo;
    }

    public Map<String, Long> getLoadedClassesInfo() {
        Map<String, Long> loadedClassesInfo = new HashMap<>();
        try {
            loadedClassesInfo.put("classes", (Long) getAttributeValue("java.lang:type=ClassLoading", "TotalLoadedClassCount"));
            loadedClassesInfo.put("classes.loaded", (long) (int) getAttributeValue("java.lang:type=ClassLoading", "LoadedClassCount"));
            loadedClassesInfo.put("classes.unloaded", (Long) getAttributeValue("java.lang:type=ClassLoading", "UnloadedClassCount"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return loadedClassesInfo;
    }

    public Map<String, Long> getGCInfo() {
        Map<String, Long> gcInfo = new HashMap<>();
        try {
            gcInfo.put("ps_scavenge.count", (Long) getAttributeValue("java.lang:type=GarbageCollector,name=PS Scavenge", "CollectionCount"));
            gcInfo.put("ps_scavenge.time", (Long) getAttributeValue("java.lang:type=GarbageCollector,name=PS Scavenge", "CollectionTime"));
            gcInfo.put("ps_marksweep.count", (Long) getAttributeValue("java.lang:type=GarbageCollector,name=PS MarkSweep", "CollectionCount"));
            gcInfo.put("ps_marksweep.time", (Long) getAttributeValue("java.lang:type=GarbageCollector,name=PS MarkSweep", "CollectionTime"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return gcInfo;
    }

    private Object getAttributeValue(String objectName, String attributeName) throws Exception {
        return jmxConnector.getMBeanServerConnection().getAttribute(new ObjectName(objectName), attributeName);
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
