package awt.jaxrs.diagnostics.error;

import javax.xml.bind.annotation.*;

import io.swagger.annotations.*;

/**
 * Object representation of an individual error message.
 *
 * @author awt
 */
@ApiModel(value = "Error", description = "Object representation of an individual error.")
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorMessage {

    @ApiModelProperty(value = "Field causing the error. Typically used only when validation fails.", required = false)
    @XmlElement(name = "field")
    private final String field;

    @ApiModelProperty(value = "Error message.")
    @XmlElement(name = "message")
    private final String message;

    public ErrorMessage(final String message, final String field) {
	this.message = message;
	this.field = field;
    }

    public ErrorMessage(final String message) {
	this(message, null);
    }

    @SuppressWarnings("unused")
    private ErrorMessage() {
	// DO NOT REMOVE! Empty constructor is mandatory for JAXB processing
	this(null, null);
    }

    public String getField() {
	return this.field;
    }

    public String getMessage() {
	return this.message;
    }

    @Override
    public String toString() {
	return "ErrorMessage [field=" + this.field + ", message=" + this.message + "]";
    }
}
