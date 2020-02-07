/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package nl.tele2.wso2is.rest.util;

import nl.tele2.wso2is.rest.Constants;
import nl.tele2.wso2is.rest.dto.*;
import nl.tele2.wso2is.rest.exception.BadRequestException;
import nl.tele2.wso2is.rest.exception.ConflictException;
import nl.tele2.wso2is.rest.exception.InternalServerErrorException;
import org.apache.commons.logging.Log;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.recovery.model.Property;
import org.wso2.carbon.identity.recovery.signup.UserSelfRegistrationManager;
import org.wso2.carbon.user.api.Claim;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Utils {

    public static UserSelfRegistrationManager getUserSelfRegistrationManager() {
        return (UserSelfRegistrationManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(UserSelfRegistrationManager.class, null);
    }


    public static String generateRandomPassword() {
        int length = 10;
        final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
        final char[] numbers = "0123456789".toCharArray();
        final char[] symbols = "!".toCharArray();
        final char[] allAllowed = "ABCDEFGJKLMNPRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!".toCharArray();

        // Use cryptographically secure random number generator
        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length - 5; i++) {
            password.append(allAllowed[random.nextInt(allAllowed.length)]);
        }

        // Ensure password policy is met by inserting required random chars in random
        // positions
        password.insert(random.nextInt(password.length()), uppercase[random.nextInt(uppercase.length)]);
        password.insert(random.nextInt(password.length()), lowercase[random.nextInt(lowercase.length)]);

        password.insert(random.nextInt(password.length()), numbers[random.nextInt(numbers.length)]);
        password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);
        return password.toString();
    }

    public static User getUser(UserDTO userDTO) {
        User user = new User();
        user.setTenantDomain(userDTO.getTenantDomain());
        if (userDTO.getRealm() == null) {
            userDTO.setRealm(IdentityUtil.getPrimaryDomainName());
        }
        user.setUserStoreDomain(userDTO.getRealm());
        user.setUserName(userDTO.getUsername());
        return user;
    }

    public static User getUser(SelfRegistrationUserDTO userDTO) {
        User user = new User();
        user.setTenantDomain(userDTO.getTenantDomain());
        user.setUserStoreDomain(userDTO.getRealm());
        user.setUserName(userDTO.getUsername());
        return user;
    }


    public static Claim[] getClaims(List<ClaimDTO> claimDTOs) {
        if (claimDTOs != null && claimDTOs.size() > 0) {
            Claim[] claims = new Claim[claimDTOs.size()];
            for (int i = 0; i < claimDTOs.size(); i++) {
                Claim claim = new Claim();
                claim.setClaimUri(claimDTOs.get(i).getUri());
                claim.setValue(claimDTOs.get(i).getValue());
                claims[i] = claim;
            }
            return claims;
        } else {
            return new Claim[0];
        }
    }

    public static String[] getRoles(List<String> roleList) {
        if (roleList == null) {
            return new String[0];
        }
        return roleList.toArray(new String[roleList.size()]);
    }


    public static Property[] getProperties(List<PropertyDTO> propertyDTOs) {
        if (propertyDTOs == null) {
            return new Property[0];
        }

        Property[] properties = new Property[propertyDTOs.size()];
        for (int i = 0; i < propertyDTOs.size(); i++) {
            Property property = new Property(propertyDTOs.get(i).getKey(), propertyDTOs.get(i).getValue());
            properties[i] = property;
        }
        return properties;
    }

    public static void handleConflict(String msg, String code) throws ConflictException {
        throw buildConflictException(msg, code);
    }

    public static ConflictException buildConflictException(String description, String code) {
        ErrorDTO errorDTO = getErrorDTO(Constants.STATUS_CONFLICT_MESSAGE_DEFAULT, code, description);
        return new ConflictException(errorDTO);
    }

    public static ErrorDTO getErrorDTO(String message, String code, String description) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setDescription(description);
        return errorDTO;
    }

    public static void handleBadRequest(String msg, String code) throws BadRequestException {
        throw buildBadRequestException(msg, code);
    }

    public static BadRequestException buildBadRequestException(String description, String code) {
        ErrorDTO errorDTO = getErrorDTO(Constants.STATUS_BAD_REQUEST_MESSAGE_DEFAULT, code, description);
        return new BadRequestException(errorDTO);
    }

    public static void handleInternalServerError(String msg, String code, Log log, Throwable throwable)
            throws InternalServerErrorException {
        InternalServerErrorException internalServerErrorException = buildInternalServerErrorException(code);
        if (throwable == null) {
            log.error(msg);
        } else {
            log.error(msg, throwable);
        }
        throw internalServerErrorException;
    }

    public static InternalServerErrorException buildInternalServerErrorException(String code) {
        ErrorDTO errorDTO = getErrorDTO(Constants.STATUS_INTERNAL_SERVER_ERROR_MESSAGE_DEFAULT, code,
                Constants.STATUS_INTERNAL_SERVER_ERROR_DESCRIPTION_DEFAULT);
        return new InternalServerErrorException(errorDTO);
    }
}
