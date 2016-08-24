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

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;

@Path("/dump")
public class DumpResource {

    @Inject
    private JmxInspector jmxInspector;

    @GET
    @Produces("application/json")
    public Response dumpStackTrace() {
        JsonArrayBuilder threadsArrayJson = Json.createArrayBuilder();
        for (ThreadInfo info : jmxInspector.getThreadInfo()) {
            JsonObjectBuilder threadJson = Json.createObjectBuilder();
            threadJson.add("threadName", info.getThreadName())
                    .add("threadId", info.getThreadId())
                    .add("blockedTime", info.getBlockedTime())
                    .add("blockedCount", info.getBlockedCount())
                    .add("waitedTime", info.getWaitedTime())
                    .add("waitedCount", info.getWaitedCount())
                    .add("lockName", nullSafe(info.getLockName()))
                    .add("lockOwnerName", nullSafe(info.getLockOwnerName()))
                    .add("inNative", info.isInNative())
                    .add("suspended", info.isSuspended())
                    .add("threadState", info.getThreadState().toString())
                    .add("stackTrace", buildStackTrace(info.getStackTrace()))
                    .add("lockedMonitors", buildLockedMonitors(info.getLockedMonitors()))
                    .add("lockedSynchronizers", buildLockedSynchronizers(info.getLockedSynchronizers()))
                    .add("lockInfo", buildLockInfo(info.getLockInfo()));
            threadsArrayJson.add(threadJson);
        }

        return Response.ok(threadsArrayJson.build()).build();
    }

    private JsonArrayBuilder buildStackTrace(StackTraceElement[] stackTrace) {
        JsonArrayBuilder stackTraceArrayJson = Json.createArrayBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            JsonObjectBuilder stackJson = Json.createObjectBuilder();
            stackJson.add("methodName", nullSafe(stackTraceElement.getMethodName()))
                    .add("fileName", nullSafe(stackTraceElement.getFileName()))
                    .add("lineNumber", stackTraceElement.getLineNumber())
                    .add("nativeMethod", stackTraceElement.isNativeMethod());
            stackTraceArrayJson.add(stackJson);
        }
        return stackTraceArrayJson;
    }

    private JsonArrayBuilder buildLockedMonitors(MonitorInfo[] lockedMonitors) {
        JsonArrayBuilder lockedMonitorsArrayJson = Json.createArrayBuilder();
        for (MonitorInfo lockedMonitor : lockedMonitors) {
            JsonObjectBuilder lockedMonitorJson = Json.createObjectBuilder();
            lockedMonitorJson.add("className", nullSafe(lockedMonitor.getClassName()))
                    .add("identityHashCode", lockedMonitor.getIdentityHashCode())
                    .add("lockedStackFrame", buildLockedStackFrame(lockedMonitor.getLockedStackFrame()))
                    .add("lockedStackDepth", lockedMonitor.getLockedStackDepth());
        }
        return lockedMonitorsArrayJson;
    }

    private JsonObjectBuilder buildLockedStackFrame(StackTraceElement lockedStackFrame) {
        JsonObjectBuilder lockedStackFrameJson = Json.createObjectBuilder();
        lockedStackFrameJson.add("methodName", nullSafe(lockedStackFrame.getMethodName()))
                .add("fileName", nullSafe(lockedStackFrame.getFileName()))
                .add("lineNumber", lockedStackFrame.getLineNumber())
                .add("className", nullSafe(lockedStackFrame.getClassName()))
                .add("nativeMethod", lockedStackFrame.isNativeMethod());
        return lockedStackFrameJson;
    }

    private JsonArrayBuilder buildLockedSynchronizers(LockInfo[] lockedSynchronizers) {
        JsonArrayBuilder lockedSynchronizersArrayJson = Json.createArrayBuilder();
        for (LockInfo lockedSynchronizer : lockedSynchronizers) {
            JsonObjectBuilder lockedSynchronizerJson = Json.createObjectBuilder();
            lockedSynchronizerJson.add("className", nullSafe(lockedSynchronizer.getClassName()))
                    .add("identityHashCode", lockedSynchronizer.getIdentityHashCode());
            lockedSynchronizersArrayJson.add(lockedSynchronizerJson);
        }
        return lockedSynchronizersArrayJson;
    }

    private JsonObjectBuilder buildLockInfo(LockInfo lockInfo) {
        JsonObjectBuilder lockInfoJson = Json.createObjectBuilder();
        if (lockInfo != null) {
            lockInfoJson.add("className", nullSafe(lockInfo.getClassName()));
            lockInfoJson.add("identityHashCode", lockInfo.getIdentityHashCode());
        }
        return lockInfoJson;
    }

    private String nullSafe(String info) {
        return info == null ? "" : info;
    }

}
