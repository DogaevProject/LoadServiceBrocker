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

import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.stereotype.Service;

/**
 * Проверяет объекты OSB API на условие соответствия спецификации.
 */
@Service
public class DefaultOsbObjectValidationService implements OsbObjectValidationService {

    @Override
    public void validateNotNullServiceDefinition(ServiceDefinition serviceDefinition, String serviceDefinitionId)
            throws ServiceBrokerInvalidParametersException {
        if (serviceDefinition == null) {
            throw new ServiceBrokerInvalidParametersException(
                    "Service Definition doesn't exist. " + "Service Definition id: " + serviceDefinitionId);
        }
    }

    @Override
    public void validateServicePlan(Plan plan, String requestPlanId) throws ServiceBrokerInvalidParametersException {
        if (plan == null) {
            throw new ServiceBrokerInvalidParametersException("Service plan doesn't exist. Plan id: " + requestPlanId);
        }
    }

}
