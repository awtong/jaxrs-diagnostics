package awt.jaxrs.diagnostics.statistics;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Statistics {
    @XmlElement(name = "key")
    private final String key;

    @XmlElement(name = "count")
    private int count;
    private long total;

    public Statistics(final String key) {
	this.key = key;
    }

    @SuppressWarnings("unused")
    private Statistics() {
	this(null);
    }

    public String getKey() {
	return this.key;
    }

    public int getCount() {
	return this.count;
    }

    public void setCount(final int count) {
	this.count = count;
    }

    public void increment() {
	this.count++;
    }

    public void addToTotal(final long timeInMillis) {
	this.total = this.total + timeInMillis;
    }

    @XmlElement(name = "avg")
    public double getAverage() {
	return ((double) this.total / this.count);
    }

    @Override
    public String toString() {
	return "Statistics [count=" + this.count + ", total=" + this.total + ", average=" + this.getAverage() + "]";
    }
}
