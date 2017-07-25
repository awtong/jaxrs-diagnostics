package awt.jaxrs.diagnostics.healthcheck;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class HealthRegistry {
    @XmlElement(name = "status")
    private Collection<HealthStatus> statuses;

    public boolean addStatus(final HealthStatus status) {
	if (this.statuses == null) {
	    this.statuses = new HashSet<>();
	}

	return this.statuses.add(status);
    }

    public Collection<HealthStatus> getStatuses() {
	return this.statuses == null ? Collections.emptySet() : this.statuses;
    }

    @XmlElement(name = "successful")
    public boolean isSuccessful() {
	if (this.statuses != null) {
	    for (final HealthStatus status : this.statuses) {
		if ((status != null) && !status.isSuccessful()) {
		    return false;
		}
	    }
	}

	return true;
    }
}
