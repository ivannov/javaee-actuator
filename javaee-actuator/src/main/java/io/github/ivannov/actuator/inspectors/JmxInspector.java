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
