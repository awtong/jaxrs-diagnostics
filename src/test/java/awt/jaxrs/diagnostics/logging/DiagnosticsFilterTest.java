package awt.jaxrs.diagnostics.logging;

import static org.mockito.Mockito.*;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;

import org.junit.*;

import awt.jaxrs.diagnostics.rules.MDCRule;

public class DiagnosticsFilterTest {

    @Rule
    public final MDCRule rule = new MDCRule();

    @Test
    public void testNoHeadersOrEntityRequestFilter() throws IOException {
	final ContainerRequestContext request = mock(ContainerRequestContext.class);
	when(request.getMethod()).thenReturn(HttpMethod.GET);

	final UriInfo info = mock(UriInfo.class);
	final URI uri = UriBuilder.fromPath("http://foo.bar").build();
	when(info.getAbsolutePath()).thenReturn(uri);
	when(request.getUriInfo()).thenReturn(info);
	final ContainerRequestFilter diagnostics = new TrafficLoggingFilter();
	diagnostics.filter(request);
    }

    @Test
    public void testEmptyHeadersNoEntityRequestFilter() throws IOException {
	final ContainerRequestContext request = mock(ContainerRequestContext.class);
	when(request.getMethod()).thenReturn(HttpMethod.GET);

	final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
	when(request.getHeaders()).thenReturn(headers);

	final UriInfo info = mock(UriInfo.class);
	final URI uri = UriBuilder.fromPath("http://foo.bar").build();
	when(info.getAbsolutePath()).thenReturn(uri);
	when(request.getUriInfo()).thenReturn(info);

	final ContainerRequestFilter diagnostics = new TrafficLoggingFilter();
	diagnostics.filter(request);
    }

    @Test
    public void testNoEntityRequestFilter() throws IOException {
	final ContainerRequestContext request = mock(ContainerRequestContext.class);
	when(request.getMethod()).thenReturn(HttpMethod.GET);

	final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
	headers.putSingle("Password", "Bar");
	headers.putSingle("Hello", "Goodbye");
	headers.addAll("Multi", "1", "2", "3");
	when(request.getHeaders()).thenReturn(headers);

	final UriInfo info = mock(UriInfo.class);
	final URI uri = UriBuilder.fromPath("http://foo.bar").build();
	when(info.getAbsolutePath()).thenReturn(uri);
	when(request.getUriInfo()).thenReturn(info);

	final ContainerRequestFilter diagnostics = new TrafficLoggingFilter();
	diagnostics.filter(request);
    }

    @Test
    public void testNullEntityRequestFilter() throws IOException {
	final ContainerRequestContext request = mock(ContainerRequestContext.class);
	when(request.getMethod()).thenReturn(HttpMethod.GET);
	when(request.hasEntity()).thenReturn(true);

	final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
	headers.putSingle("Foo", "Bar");
	headers.putSingle("Hello", "Goodbye");
	headers.addAll("Multi", "1", "2", "3");
	when(request.getHeaders()).thenReturn(headers);

	final UriInfo info = mock(UriInfo.class);
	final URI uri = UriBuilder.fromPath("http://foo.bar").build();
	when(info.getAbsolutePath()).thenReturn(uri);
	when(request.getUriInfo()).thenReturn(info);

	final ContainerRequestFilter diagnostics = new TrafficLoggingFilter();
	diagnostics.filter(request);
    }

    @Test
    public void testRequestFilter() throws IOException {
	final ContainerRequestContext request = mock(ContainerRequestContext.class);
	when(request.getMethod()).thenReturn(HttpMethod.GET);
	when(request.hasEntity()).thenReturn(true);
	when(request.getEntityStream())
	.thenReturn(new ByteArrayInputStream(StandardCharsets.UTF_8.encode("My Payload").array()));
	final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
	headers.putSingle("Foo", "Bar");
	headers.putSingle("Hello", "Goodbye");
	headers.addAll("Multi", "1", "2", "3");
	when(request.getHeaders()).thenReturn(headers);

	final UriInfo info = mock(UriInfo.class);
	final URI uri = UriBuilder.fromPath("http://foo.bar").build();
	when(info.getAbsolutePath()).thenReturn(uri);
	when(request.getUriInfo()).thenReturn(info);

	final ContainerRequestFilter diagnostics = new TrafficLoggingFilter();
	diagnostics.filter(request);
    }
}
