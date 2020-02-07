package nl.tele2.wso2is.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class PropertyDTO {
    private String key = null;
    private String value = null;

    @ApiModelProperty(value = "")
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        return "class PropertyDTO {\n" +
                "  key: " + key + "\n" +
                "  value: " + value + "\n" +
                "}\n";
    }
}
