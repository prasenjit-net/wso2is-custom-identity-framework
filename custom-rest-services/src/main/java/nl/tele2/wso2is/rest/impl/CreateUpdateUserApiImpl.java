package nl.tele2.wso2is.rest.impl;

import nl.tele2.wso2is.rest.CreateUpdateUserApiService;
import nl.tele2.wso2is.rest.dto.*;
import nl.tele2.wso2is.rest.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.recovery.internal.IdentityRecoveryServiceDataHolder;
import org.wso2.carbon.identity.user.export.core.UserExportException;

import org.wso2.carbon.user.api.Claim;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateUpdateUserApiImpl extends CreateUpdateUserApiService {
    private static final Log LOG = LogFactory.getLog(CreateUpdateUserApiImpl.class);

    private static final String SHOP_CLAIM_URI = "http://wso2.org/claims/shopid";
    private static final String CUSTOMERTYPE_CLAIM_URI = "http://wso2.org/claims/customer_type";
    private static final String ROLE_CLAIM_URI = "http://wso2.org/claims/user_role";
    private static final String ACTIVE_CLAIM_URI = "http://wso2.org/claims/identity/accountLocked";
    private static final String EMAIL_CLAIM_URI = "http://wso2.org/claims/emailaddress";
    protected static final String CHALLENGE_QUESTION_URIS_CLAIM = "http://wso2.org/claims/challengeQuestionUris";
    protected static final String QUESTION_CHALLENGE_SEPARATOR = "Recovery.Question.Password.Separator";
    protected static final String DEFAULT_CHALLENGE_QUESTION_SEPARATOR = "!";


    public Response createUpdateUserPost(CreateUpdateUserRequestDTO createUpdateUserRequestDTO) throws UserStoreException {
        LOG.info("inside createUpdateUserPost ....");
        LOG.info(createUpdateUserRequestDTO.getEmail());
        LOG.info(createUpdateUserRequestDTO.getCustomerType());
        LOG.info(createUpdateUserRequestDTO.getResellerShopId());
        LOG.info(createUpdateUserRequestDTO.getRole());
        RealmService realmService = IdentityRecoveryServiceDataHolder.getInstance().getRealmService();
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        LOG.info("tenantId ...."+tenantId);
        org.wso2.carbon.user.core.UserStoreManager userStoreManager = null;

        Response response = null;
        try {
            if (realmService.getTenantUserRealm(tenantId) != null) {
                userStoreManager = (org.wso2.carbon.user.core.UserStoreManager) realmService.getTenantUserRealm
                        (tenantId).getUserStoreManager();
                LOG.info(userStoreManager);
                LOG.info("existing user ...."+userStoreManager.isExistingUser(createUpdateUserRequestDTO.getEmail()));
                if(userStoreManager.isExistingUser(createUpdateUserRequestDTO.getEmail())){
                    response = updateUser(userStoreManager,createUpdateUserRequestDTO);
                    userStoreManager.getUserClaimValue(createUpdateUserRequestDTO.getEmail(),ACTIVE_CLAIM_URI,"default");
                }else{
                    response = createAndRegisterUser(userStoreManager,createUpdateUserRequestDTO);
                }

            }
        } catch (Exception e) {
            String msg = "Error retrieving the user-list for the tenant : " + tenantId;
        }
        return response;

    }

    public Response createAndRegisterUser(UserStoreManager userStoreManager, CreateUpdateUserRequestDTO createUpdateUserRequestDTO) throws UserStoreException, UserExportException {
        LOG.info("inside createAndRegisterUser ....");
        SelfRegistrationUserDTO selfRegistrationUserDTO = new SelfRegistrationUserDTO();

        selfRegistrationUserDTO.setUsername(createUpdateUserRequestDTO.getEmail());
        String password = Utils.generateRandomPassword();
        selfRegistrationUserDTO.setPassword(password);
        List<ClaimDTO> claims = new ArrayList();

        ClaimDTO resellerShopIdClaim = new ClaimDTO();
        resellerShopIdClaim.setUri(SHOP_CLAIM_URI);
        resellerShopIdClaim.setValue(createUpdateUserRequestDTO.getResellerShopId());

        claims.add(resellerShopIdClaim);

        ClaimDTO customerTypeClaim = new ClaimDTO();
        customerTypeClaim.setUri(CUSTOMERTYPE_CLAIM_URI);
        customerTypeClaim.setValue(createUpdateUserRequestDTO.getCustomerType());

        claims.add(customerTypeClaim);

        ClaimDTO roleClaim = new ClaimDTO();
        roleClaim.setUri(ROLE_CLAIM_URI);
        roleClaim.setValue(createUpdateUserRequestDTO.getRole());
        claims.add(roleClaim);

        ClaimDTO emailClaim = new ClaimDTO();
        emailClaim.setUri(EMAIL_CLAIM_URI+"@tele2.com");
        emailClaim.setValue(createUpdateUserRequestDTO.getEmail());
        claims.add(emailClaim);

        String tenantDomain = MultitenantUtils.getTenantDomain(createUpdateUserRequestDTO.getEmail());

        ClaimDTO usernameClaim = new ClaimDTO();
        usernameClaim.setUri("http://wso2.org/claims/username");
        usernameClaim.setValue(createUpdateUserRequestDTO.getEmail()+"@"+tenantDomain);
        claims.add(usernameClaim);


        LOG.info("claims set ...");

        selfRegistrationUserDTO.setClaims(claims);
        SelfUserRegistrationRequestDTO selfUserRegistrationRequestDTO = new SelfUserRegistrationRequestDTO();
        selfUserRegistrationRequestDTO.setUser(selfRegistrationUserDTO);

        MeApiService delegate = MeApiServiceFactory.getMeApi();
        Response createUserResp = delegate.mePost(selfUserRegistrationRequestDTO);

        LOG.info(createUserResp.getStatus());
        if(createUserResp.getStatus()< 200 || createUserResp.getStatus() >299){
            return createUserResp;
        }
        LOG.info("createUserResp ..."+createUserResp);
        Map<String, String> attributes = getClaims(userStoreManager,createUpdateUserRequestDTO);

        for (Map.Entry<String,String> entry : attributes.entrySet()){
            LOG.info("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        }

        CreateUpdateUserResponseDTO createUpdateUserResponseDTO = new CreateUpdateUserResponseDTO();
        createUpdateUserResponseDTO.setUid(String.valueOf(userStoreManager.getUserId(createUpdateUserRequestDTO.getEmail())));
        createUpdateUserResponseDTO.setEmail(createUpdateUserRequestDTO.getEmail()+"@tele2.com");
        createUpdateUserResponseDTO.setRole(attributes.get(ROLE_CLAIM_URI));
        createUpdateUserResponseDTO.setCustomerType(attributes.get(CUSTOMERTYPE_CLAIM_URI));
        if("true".equalsIgnoreCase(attributes.get(ACTIVE_CLAIM_URI))){
            createUpdateUserResponseDTO.setActive("false");
        }else{
            createUpdateUserResponseDTO.setActive("true");
        }

        createUpdateUserResponseDTO.setResellerShopId(attributes.get(SHOP_CLAIM_URI));

        return Response.ok().status(Response.Status.OK).entity(createUpdateUserResponseDTO).build();


    }

    public Response updateUser(UserStoreManager userStoreManager, CreateUpdateUserRequestDTO createUpdateUserRequestDTO) throws UserStoreException, UserExportException {
        LOG.info("Inside updateUser ....");
        userStoreManager.setUserClaimValue(createUpdateUserRequestDTO.getEmail(), SHOP_CLAIM_URI, createUpdateUserRequestDTO.getResellerShopId(), "default");
        userStoreManager.setUserClaimValue(createUpdateUserRequestDTO.getEmail(), CUSTOMERTYPE_CLAIM_URI, createUpdateUserRequestDTO.getCustomerType(), "default");
        userStoreManager.setUserClaimValue(createUpdateUserRequestDTO.getEmail(), ROLE_CLAIM_URI, createUpdateUserRequestDTO.getRole(), "default");

        Map<String, String> attributes = getClaims(userStoreManager,createUpdateUserRequestDTO);

        CreateUpdateUserResponseDTO createUpdateUserResponseDTO = new CreateUpdateUserResponseDTO();
        createUpdateUserResponseDTO.setUid(String.valueOf(userStoreManager.getUserId(createUpdateUserRequestDTO.getEmail())));
        createUpdateUserResponseDTO.setEmail(createUpdateUserRequestDTO.getEmail());
        createUpdateUserResponseDTO.setRole(attributes.get(ROLE_CLAIM_URI));
        createUpdateUserResponseDTO.setCustomerType(attributes.get(CUSTOMERTYPE_CLAIM_URI));

        if("true".equalsIgnoreCase(attributes.get(ACTIVE_CLAIM_URI))){
            createUpdateUserResponseDTO.setActive("false");
        }else{
            createUpdateUserResponseDTO.setActive("true");
        }        createUpdateUserResponseDTO.setResellerShopId(attributes.get(SHOP_CLAIM_URI));

        return Response.ok().status(Response.Status.OK).entity(createUpdateUserResponseDTO).build();
    }
    protected List<String> getChallengeQuestionUris(Map<String, String> attributes) {

        String challengeQuestionUrisClaim = attributes.get(CHALLENGE_QUESTION_URIS_CLAIM);
        return getChallengeQuestionUris(challengeQuestionUrisClaim);
    }
    protected String challengeQuestionSeparator() {

        String challengeQuestionSeparator = IdentityUtil.getProperty(QUESTION_CHALLENGE_SEPARATOR);

        if (StringUtils.isEmpty(challengeQuestionSeparator)) {
            challengeQuestionSeparator = DEFAULT_CHALLENGE_QUESTION_SEPARATOR;
        }
        return challengeQuestionSeparator;
    }
    protected List<String> getChallengeQuestionUris(String challengeQuestionUrisClaim) {

        if (StringUtils.isNotEmpty(challengeQuestionUrisClaim)) {
            String challengeQuestionSeparator = challengeQuestionSeparator();

            String[] challengeQuestionUriList = challengeQuestionUrisClaim.split(challengeQuestionSeparator);
            return Arrays.asList(challengeQuestionUriList);
        } else {
            return new ArrayList<>();
        }

    }
    protected  Map<String, String> getClaims(UserStoreManager userStoreManager, CreateUpdateUserRequestDTO createUpdateUserRequestDTO) throws UserExportException {
        Claim[] userClaimValues;
        Map<String, String> attributes = null;
        try {
            userClaimValues = userStoreManager.getUserClaimValues(createUpdateUserRequestDTO.getEmail(), null);
        } catch (UserStoreException e) {
            throw new UserExportException("Error while retrieving the user information.", e);
        }
        CreateUpdateUserResponseDTO createUpdateUserResponseDTO = new CreateUpdateUserResponseDTO();

        if (userClaimValues != null) {
            attributes = Arrays.stream(userClaimValues).collect(Collectors.toMap
                    (Claim::getClaimUri, Claim::getValue));

            List<String> challengeQuestionUris = getChallengeQuestionUris(attributes);
            if (challengeQuestionUris.size() > 0) {
                for (String challengeQuestionUri : challengeQuestionUris) {
                    attributes.remove(challengeQuestionUri);
                }
            }

            attributes.remove(CHALLENGE_QUESTION_URIS_CLAIM);
            LOG.info(attributes.size());

        }
        return attributes;
    }
}
