package io.github.ivannov.actuator.resources;

import io.github.ivannov.actuator.inspectors.JmxInspector;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @author Ivan St. Ivanov.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MetricsResourceTest extends ActuatorResourcesBaseTest {

    static final int EXPECTED_CPU = 42;

    static final int EXPECTED_USED_HEAP = 231_000;
    static final int EXPECTED_COMMITTED_HEAP = 328_000;
    static final int EXPECTED_INIT_HEAP = 258_000;
    static final int EXPECTED_MAX_HEAP = 3_600_000;

    static final int EXPECTED_THREADS = 30;
    static final int EXPECTED_THREADS_PEAK = 30;
    static final int EXPECTED_THREADS_DAEMON = 29;

    static final int EXPECTED_CLASSES = 6422;
    static final int EXPECTED_LOADED_CLASSES = 6400;
    static final int EXPECTED_UNLOADED_CLASSES = 76;

    static final int EXPECTED_GC_PS_SCAVENGE_TIME = 46;
    static final int EXPECTED_GC_PS_SCAVENGE_COUNT = 11;
    static final int EXPECTED_GC_PS_MARKSWEEP_TIME = 199;
    static final int EXPECTED_GC_PS_MARKSWEEP_COUNT = 7;

    @Deployment
    public static WebArchive createDeployment() {
        return initDeployment()
                .addClasses(JmxInspector.class, MetricsResource.class)
                .addClass(JmxInspectorStub.class);
    }

    @Test
    public void metricsResourceShouldHaveProperStructure() throws Exception {
        Response response = sendRequest("metrics");
        JsonObject metricsResourceContent = readJsonContent(response);
        assertCpu(metricsResourceContent);
        assertMemory(metricsResourceContent);
        assertThreadDetails(metricsResourceContent);
        assertLoadedClasses(metricsResourceContent);
        assertGCInfo(metricsResourceContent);
    }

    private void assertCpu(JsonObject metricsResourceContent) {
        assertEquals(EXPECTED_CPU, metricsResourceContent.getInt("cpu"));
    }

    private void assertMemory(JsonObject metricsResourceContent) {
        assertEquals(EXPECTED_USED_HEAP, metricsResourceContent.getInt("heap.used"));
        assertEquals(EXPECTED_COMMITTED_HEAP, metricsResourceContent.getInt("heap.committed"));
        assertEquals(EXPECTED_INIT_HEAP, metricsResourceContent.getInt("heap.init"));
        assertEquals(EXPECTED_MAX_HEAP, metricsResourceContent.getInt("heap.max"));
    }

    private void assertThreadDetails(JsonObject metricsResourceContent) {
        assertEquals(EXPECTED_THREADS, metricsResourceContent.getInt("threads"));
        assertEquals(EXPECTED_THREADS_PEAK, metricsResourceContent.getInt("threads.peak"));
        assertEquals(EXPECTED_THREADS_DAEMON, metricsResourceContent.getInt("threads.daemon"));
    }

    private void assertLoadedClasses(JsonObject metricsResourceContent) {
        assertEquals(EXPECTED_CLASSES, metricsResourceContent.getInt("classes"));
        assertEquals(EXPECTED_LOADED_CLASSES, metricsResourceContent.getInt("classes.loaded"));
        assertEquals(EXPECTED_UNLOADED_CLASSES, metricsResourceContent.getInt("classes.unloaded"));
    }

    private void assertGCInfo(JsonObject metricsResourceContent) {
        assertEquals(EXPECTED_GC_PS_SCAVENGE_COUNT, metricsResourceContent.getInt("gc.ps_scavenge.count"));
        assertEquals(EXPECTED_GC_PS_SCAVENGE_TIME, metricsResourceContent.getInt("gc.ps_scavenge.time"));
        assertEquals(EXPECTED_GC_PS_MARKSWEEP_COUNT, metricsResourceContent.getInt("gc.ps_marksweep.count"));
        assertEquals(EXPECTED_GC_PS_MARKSWEEP_TIME, metricsResourceContent.getInt("gc.ps_marksweep.time"));
    }
}
