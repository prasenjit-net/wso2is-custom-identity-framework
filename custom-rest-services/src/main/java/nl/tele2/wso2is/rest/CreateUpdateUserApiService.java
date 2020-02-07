package nl.tele2.wso2is.rest;

import nl.tele2.wso2is.rest.dto.CreateUpdateUserRequestDTO;
import org.wso2.carbon.user.api.UserStoreException;

import javax.ws.rs.core.Response;

public abstract class CreateUpdateUserApiService {
    public abstract Response createUpdateUserPost(CreateUpdateUserRequestDTO user) throws UserStoreException;
}
