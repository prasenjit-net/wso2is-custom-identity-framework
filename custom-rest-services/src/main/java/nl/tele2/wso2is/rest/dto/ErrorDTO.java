package nl.tele2.wso2is.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class ErrorDTO {
    private String code = null;
    private String message = null;
    private String description = null;

    @ApiModelProperty(value = "")
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {

        return "class ErrorDTO {\n" +
                "  code: " + code + "\n" +
                "  message: " + message + "\n" +
                "  description: " + description + "\n" +
                "}\n";
    }
}
