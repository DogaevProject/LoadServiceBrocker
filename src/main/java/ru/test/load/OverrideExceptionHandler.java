/*
 * (c) 2019 Mobile TeleSystems PJSC, Russia
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Mobile TeleSystems PJSC ("MTS" - NYSE:MBT; MOEX:MTSS)
 * and its suppliers, if any. The intellectual and technical concepts
 * contained herein are proprietary to Mobile TeleSystems PJSC
 * and its suppliers, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Mobile TeleSystems PJSC.
 */
package ru.test.load;

import org.springframework.cloud.servicebroker.annotation.ServiceBrokerRestController;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.cloud.servicebroker.exception.ServiceDefinitionDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.model.error.ErrorMessage;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Это временное решение для переопределения кода ошибки.
 */
@ControllerAdvice(annotations = ServiceBrokerRestController.class)
@ResponseBody
@Order(Ordered.LOWEST_PRECEDENCE - 11)
public class OverrideExceptionHandler {

    @ExceptionHandler(ServiceInstanceDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleException(ServiceInstanceDoesNotExistException ex) {
        return getErrorResponse(ex);
    }

    private static final String MISSING_REQUEST_BODY = "Required request body is missing";

    @ExceptionHandler(ServiceBrokerInvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleException(ServiceBrokerInvalidParametersException ex) {
        return getErrorResponse(ex);
    }

    @ExceptionHandler(ServiceDefinitionDoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleException(ServiceDefinitionDoesNotExistException ex) {
        return getErrorResponse(ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleException(HttpMessageNotReadableException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains(MISSING_REQUEST_BODY)) {
            return new ErrorMessage(MISSING_REQUEST_BODY);
        }

        return new ErrorMessage(ex.getMessage());
    }

    protected ErrorMessage getErrorResponse(ServiceBrokerException ex) {
        return ex.getErrorMessage();
    }
}
