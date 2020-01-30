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

package nl.tele2.wso2is.rest.exception;

import nl.tele2.wso2is.rest.Constants;
import nl.tele2.wso2is.rest.dto.ErrorDTO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Exception class indicating not found errors
 */
public class NotFoundException extends WebApplicationException {

    private String message;

    /**
     * Constructs a new exception from the ErrorDTO{@link ErrorDTO} object.
     *
     * @param errorDTO ErrorDTO{@link ErrorDTO} object holding the error code and the message
     */
    public NotFoundException(ErrorDTO errorDTO) {
        super(Response.status(Response.Status.NOT_FOUND)
                .entity(errorDTO)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.DEFAULT_RESPONSE_CONTENT_TYPE)
                .build());
        message = errorDTO.getDescription();
    }

    /**
     * Constructs a new exception instance.
     *
     */
    public NotFoundException() {
        super(Response.Status.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
