package awt.jaxrs.diagnostics.headers;

public enum Header {
    X_CALL_ID("X-Call-ID"), X_CORRELATION_ID("X-Correlation-ID");

    private final String name;

    private Header(final String name) {
	this.name = name;
    }

    public String getName() {
	return this.name;
    }
}
