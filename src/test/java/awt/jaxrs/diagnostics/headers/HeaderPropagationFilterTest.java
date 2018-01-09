package awt.jaxrs.diagnostics.headers;

import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.container.*;
import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HeaderPropagationFilterTest {

    @Mock
    private ContainerRequestContext request;

    @Mock
    private ContainerResponseContext response;

    @Test
    public void test() throws IOException {
	when(this.response.getHeaders()).thenReturn(new MultivaluedHashMap<String, Object>());
	final ContainerResponseFilter crf = new HeaderPropagationFilter();
	crf.filter(this.request, this.response);
    }

}
