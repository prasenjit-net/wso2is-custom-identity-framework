package nl.tele2.wso2is.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;


@ApiModel(description = "")
public class SelfRegistrationUserDTO {
    private String username = null;
    private String tenantDomain = null;
    private String realm = null;
    private String password = null;
    private List<ClaimDTO> claims = new ArrayList<ClaimDTO>();

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

    @ApiModelProperty(value = "")
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("claims")
    public List<ClaimDTO> getClaims() {
        return claims;
    }

    public void setClaims(List<ClaimDTO> claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return "class SelfRegistrationUserDTO {\n" +
                "  username: " + username + "\n" +
                "  tenantDomain: " + tenantDomain + "\n" +
                "  realm: " + realm + "\n" +
                "  password: " + password + "\n" +
                "  claims: " + claims + "\n" +
                "}\n";
    }
}
