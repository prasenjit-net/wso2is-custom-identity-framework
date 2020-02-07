package nl.tele2.wso2is.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "")
public class CreateUpdateUserRequestDTO {
    private String email;
    private String tempEmail;
    private String role;
    private String customerType;
    private String resellerShopId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTempEmail() {
        return tempEmail;
    }

    public void setTempEmail(String tempEmail) {
        this.tempEmail = tempEmail;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getResellerShopId() {
        return resellerShopId;
    }

    public void setResellerShopId(String resellerShopId) {
        this.resellerShopId = resellerShopId;
    }

}


