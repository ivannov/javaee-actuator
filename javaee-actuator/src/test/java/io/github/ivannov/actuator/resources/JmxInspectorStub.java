package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.JmxInspector;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ThreadInfo;
import java.util.HashMap;
import java.util.Map;

import static io.github.ivannov.actuator.resources.MetricsResourceTest.*;

/**
 * @author Ivan St. Ivanov.
 */
@ApplicationScoped
public class JmxInspectorStub implements JmxInspector {

    public int getProcessCPU() {
        return EXPECTED_CPU;
    }

    public Map<String, Long> getHeapMemory() {
        Map<String, Long> heapMemoryMap = new HashMap<>();
        heapMemoryMap.put("used", (long) EXPECTED_USED_HEAP);
        heapMemoryMap.put("committed", (long)  EXPECTED_COMMITTED_HEAP);
        heapMemoryMap.put("init", (long) EXPECTED_INIT_HEAP);
        heapMemoryMap.put("max", (long) EXPECTED_MAX_HEAP);
        return heapMemoryMap;
    }

    public Map<String, Integer> getThreadDetails() {
        Map<String, Integer> threadDetails = new HashMap<>();
        threadDetails.put("threads", EXPECTED_THREADS);
        threadDetails.put("threads.peak", EXPECTED_THREADS_PEAK);
        threadDetails.put("threads.daemon", EXPECTED_THREADS_DAEMON);
        return threadDetails;
    }

    public ThreadInfo[] getThreadInfo() {
        return null;
    }

    public Map<String, Long> getLoadedClassesInfo() {
        Map<String, Long> loadedClassesInfo = new HashMap<>();
        loadedClassesInfo.put("classes", (long) EXPECTED_CLASSES);
        loadedClassesInfo.put("classes.loaded", (long)  EXPECTED_LOADED_CLASSES);
        loadedClassesInfo.put("classes.unloaded", (long) EXPECTED_UNLOADED_CLASSES);
        return loadedClassesInfo;
    }

    public Map<String, Long> getGCInfo() {
        Map<String, Long> gcInfo = new HashMap<>();
        gcInfo.put("ps_scavenge.count", (long) EXPECTED_GC_PS_SCAVENGE_COUNT);
        gcInfo.put("ps_scavenge.time", (long) EXPECTED_GC_PS_SCAVENGE_TIME);
        gcInfo.put("ps_marksweep.count", (long) EXPECTED_GC_PS_MARKSWEEP_COUNT);
        gcInfo.put("ps_marksweep.time", (long) EXPECTED_GC_PS_MARKSWEEP_TIME);
        return gcInfo;
    }
}

