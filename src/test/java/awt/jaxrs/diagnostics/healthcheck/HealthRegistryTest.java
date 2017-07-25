package awt.jaxrs.diagnostics.healthcheck;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HealthRegistryTest {

    @Test
    public void testNoStatuses() {
	final HealthRegistry registry = new HealthRegistry();
	assertThat(registry.isSuccessful(), is(true));
    }

    @Test
    public void testAllSuccessfulStatuses() {
	final HealthRegistry registry = new HealthRegistry();
	final HealthStatus s1 = new HealthStatus("1");
	s1.setSuccessful(true);
	final HealthStatus s2 = new HealthStatus("2");
	s2.setSuccessful(true);
	registry.addStatus(s1);
	registry.addStatus(s2);
	registry.addStatus(null);
	assertThat(registry.isSuccessful(), is(true));
    }

    @Test
    public void testNotAllSuccessfulStatuses() {
	final HealthRegistry registry = new HealthRegistry();
	final HealthStatus s1 = new HealthStatus("1");
	s1.setSuccessful(true);
	final HealthStatus s2 = new HealthStatus("2");
	s2.setSuccessful(false);
	registry.addStatus(s1);
	registry.addStatus(s2);
	registry.addStatus(null);
	assertThat(registry.isSuccessful(), is(false));
    }
}
