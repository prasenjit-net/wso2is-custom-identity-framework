package nl.tele2.wso2is.rest.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "")
public class UserDTO {
    private String username = null;
    private String tenantDomain = null;
    private String realm = null;

    @ApiModelProperty(value = "")
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("tenant-domain")
    public String getTenantDomain() {
        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("realm")
    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDTO {\n");

        sb.append("  username: ").append(username).append("\n");
        sb.append("  tenantDomain: ").append(tenantDomain).append("\n");
        sb.append("  realm: ").append(realm).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
