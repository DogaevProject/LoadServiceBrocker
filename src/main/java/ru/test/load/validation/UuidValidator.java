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
package ru.test.load.validation;

import org.slf4j.Logger;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

import java.util.UUID;

/**
 * Class with static method for UUID strings validation.
 */
public final class UuidValidator {

    private UuidValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Validates that the specified uuidString is really a UUID.
     * @param uuidString    uuidString to be validated
     * @param parameterName the name of parameger to be written in the log
     * @param logger        logger to use
     * @return the same uuidString if the validation has passed successfully
     * @throws ServiceBrokerInvalidParametersException if provided uuidString is not a UUID
     */
    public static void validateUuidOrThrowException(
            final String uuidString, final String parameterName, final Logger logger) {

        logger.debug("Checking a {} is a valid UUID: {}", parameterName, uuidString);
        try {
            UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            logger.error("{} provided is not a valid UUID as specified by the OSB API specification", parameterName, e);
            throw new ServiceBrokerInvalidParametersException(e);
        }
    }
}
