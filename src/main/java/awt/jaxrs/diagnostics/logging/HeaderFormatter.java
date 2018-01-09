package awt.jaxrs.diagnostics.logging;

import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;

public class HeaderFormatter {
    private static final Collection<String> IGNORED = new ArrayList<>();

    private final MultivaluedMap<String, ?> headers;

    static {
	IGNORED.add("password");
	IGNORED.add("pwd");
    }

    public HeaderFormatter(final MultivaluedMap<String, ?> headers) {
	this.headers = headers;
    }

    public String join() {
	if (this.headers == null) {
	    return null;
	}

	final StringJoiner joiner = new StringJoiner(", ");
	this.headers.forEach((key, values) -> {
	    if ((key != null) && !IGNORED.stream().anyMatch(key::equalsIgnoreCase)) {
		joiner.add("[" + key + ": "
			+ (values == null ? ""
				: values.stream().map(value -> value == null ? "" : value.toString())
				.collect(Collectors.joining(", ")))
			+ "]");
	    }
	});

	return joiner.toString();
    }
}
