package ru.test.load.config;

import ru.test.load.plan.LoadPlan;
import org.springframework.cloud.servicebroker.model.catalog.Catalog;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
public class ServiceCatalogConfiguration {

    private static final String SERVICE_ID = "acdd7cfc-ccd9-11ea-87d0-0242ac130005";

    private static final String SERVICE_NAME = "load-testing";

    @Bean
    public Catalog catalog() {
        List<Plan> plans = Stream.of(LoadPlan.values()).map(plan -> Plan.builder()
                .id(plan.getPlanId())
                .name(plan.getPlanName())
                .description(plan.getPlanDescription())
                .free(true)
                .metadata("displayName", plan.getPlanName())
                .build()).collect(Collectors.toList());

        ServiceDefinition serviceDefinition = ServiceDefinition.builder()
                .id(SERVICE_ID)
                .name(SERVICE_NAME)
                .description("Модуль нагрузочного тестирования")
                .bindable(true)
                .instancesRetrievable(false)
                .bindingsRetrievable(false)
                .planUpdateable(false)
                .tags(SERVICE_NAME, "testing")
                .metadata("providerDisplayName", "Test Project")
                .plans(plans)
                .build();

        return Catalog.builder()
                .serviceDefinitions(serviceDefinition)
                .build();
    }
}
