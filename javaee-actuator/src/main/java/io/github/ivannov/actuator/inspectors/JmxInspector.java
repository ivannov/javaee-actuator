package io.github.ivannov.actuator.inspectors;

import java.lang.management.ThreadInfo;
import java.util.Map;

public interface JmxInspector {

    int getProcessCPU();

    Map<String, Long> getHeapMemory();

    Map<String, Integer> getThreadDetails();

    ThreadInfo[] getThreadInfo();

    Map<String, Long> getLoadedClassesInfo();

    Map<String, Long> getGCInfo();
}
