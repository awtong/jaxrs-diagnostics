package awt.jaxrs.diagnostics.logging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;

import javax.ws.rs.core.*;

import org.junit.Test;

public class HeaderFormatterTest {

    @Test
    public void testNullHeaders() {
	final HeaderFormatter formatter = new HeaderFormatter(null);
	assertThat(formatter.join(), is(nullValue()));
    }

    @Test
    public void testFormatter() {
	final MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();
	map.putSingle("Key1", "Value1");
	map.putSingle("Key2", "Value2");
	map.put("Key3", null);

	final List<Object> list = new ArrayList<>();
	list.add(null);
	map.put("Key4", list);
	final HeaderFormatter formatter = new HeaderFormatter(map);
	final String joined = formatter.join();
	assertThat(joined, is(notNullValue()));

	// XXX how to test joined is correct?
    }
}
