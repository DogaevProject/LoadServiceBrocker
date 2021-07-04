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

public interface OsbObjectValidationService {
    /**
     * Проверяет Описание Сервиса на <code>null</code>. Если модель Описания Сервиса - <code>null</code>,
     * создается исключение {@link ServiceBrokerInvalidParametersException}. Это означает,
     * что такого Описания Сервиса не существует.
     *
     * @param serviceDefinition модель Описания Сервиса
     * @param serviceDefinitionId идентификатор Описания Сервиса
     */
    void validateNotNullServiceDefinition(ServiceDefinition serviceDefinition, String serviceDefinitionId)
            throws ServiceBrokerInvalidParametersException;

    /**
     * Проверяет Сервис План на <code>null</code>. Если модель Сервис Плана - <code>null</code> создается исключение
     * {@link ServiceBrokerInvalidParametersException}. Это означает, что такого Сервис Плана не существует.
     *
     * @param plan модель Сервис Плана
     * @param requestPlanId идентификатор Сервис Плана
     */
    void validateServicePlan(Plan plan, String requestPlanId) throws ServiceBrokerInvalidParametersException;
}
