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

import io.github.ivannov.actuator.inspectors.JmxInspector;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ThreadInfo;
import java.util.HashMap;
import java.util.Map;

import static io.github.ivannov.actuator.resources.MetricsResourceTest.*;

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

