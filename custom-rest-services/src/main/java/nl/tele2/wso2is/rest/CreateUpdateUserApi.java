package nl.tele2.wso2is.rest;

import io.swagger.annotations.ApiParam;
import nl.tele2.wso2is.rest.dto.CreateUpdateUserRequestDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.user.api.UserStoreException;
import nl.tele2.wso2is.rest.factories.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/createUpdateUser")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(value = "/createUpdateUser", description = "the createUpdateUser API")
public class CreateUpdateUserApi  {
    private static final Log LOG = LogFactory.getLog(CreateUpdateUserApi.class);

    private final CreateUpdateUserApiService delegate = CreateUpdateUserFactory.getCreateUpdateUserApi();

    @POST

    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "This API is used to user self registration. \n", response = String.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Successful created"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })

    public Response mePost(@ApiParam(value = "It can be sent optional property parameters over email based on email template." ,required=true ) CreateUpdateUserRequestDTO createUpdateUserRequestDTO) throws UserStoreException {
        LOG.info("Inside CreateUpdateUserApi ..");
        return delegate.createUpdateUserPost(createUpdateUserRequestDTO);

    }
}