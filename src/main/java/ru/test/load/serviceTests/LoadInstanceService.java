package ru.test.load.serviceTests;

import ru.test.load.config.ServiceBrokerConfiguration;
import ru.test.load.model.LoadServiceBinding;
import ru.test.load.repository.binding.ServiceBindingRepository;
import ru.test.load.repository.instance.ServiceInstanceRepository;
import ru.test.load.validation.OsbObjectValidationService;
import ru.test.load.validation.UuidValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.instance.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class LoadInstanceService implements ServiceInstanceService {

    private static final String LOG_MSG_RESPONSE = "Response is going to be sent: {}";

    private static final Logger logger = LoggerFactory.getLogger(LoadInstanceService.class);
    private final OsbObjectValidationService osbObjectValidationService;
    private final ServiceInstanceRepository<ServiceInstance> serviceInstanceRepository;
    private final ServiceBindingRepository<LoadServiceBinding> serviceBindingRepository;
    private final ServiceBrokerConfiguration brokerConfig;

    @Autowired
    public LoadInstanceService(OsbObjectValidationService osbObjectValidationService, ServiceInstanceRepository<ServiceInstance> serviceInstanceRepository, ServiceBindingRepository<LoadServiceBinding> serviceBindingRepository, ServiceBrokerConfiguration brokerConfig) {
        this.osbObjectValidationService = osbObjectValidationService;
        this.serviceInstanceRepository = serviceInstanceRepository;
        this.serviceBindingRepository = serviceBindingRepository;
        this.brokerConfig = brokerConfig;
    }

    @Override
    public Mono<CreateServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest request) {
        logger.debug("Request to create service instance: {}", request);

        osbObjectValidationService.validateNotNullServiceDefinition(request.getServiceDefinition(), request.getServiceDefinitionId());
        osbObjectValidationService.validateServicePlan(request.getPlan(), request.getPlanId());

        final String serviceInstanceId = request.getServiceInstanceId();
        if (brokerConfig.isUuidValidationEnabled()) {
            UuidValidator.validateUuidOrThrowException(serviceInstanceId, "ServiceInstanceId", logger);
        }

        CreateServiceInstanceResponse.CreateServiceInstanceResponseBuilder responseBuilder = CreateServiceInstanceResponse.builder().async(false);

        //смотрим, существует ли такой инстанс
        final ServiceInstance serviceInstanceInfo = serviceInstanceRepository.putIfAbsent(new ServiceInstance(serviceInstanceId, request.getServiceDefinitionId(), request.getPlanId()));

        //если существует, то отменяем создание и кидаем ошибку, в противном случае пытаемся создать инстанс
        if (serviceInstanceInfo != null) {
            logger.error("instance already exists");
            responseBuilder.instanceExisted(hasServiceInstanceInfoTheSameParams(serviceInstanceInfo, request.getServiceDefinitionId(), request.getPlanId(), request.getServiceInstanceId()));
        }
        else {
            serviceInstanceRepository.save(new ServiceInstance(serviceInstanceId, request.getServiceDefinitionId(), request.getPlanId()));
            responseBuilder.instanceExisted(false);
        }
        CreateServiceInstanceResponse response = responseBuilder.build();

        logger.debug(LOG_MSG_RESPONSE, response);
        return Mono.just(response);
    }

    @Override
    public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
        logger.debug("Request to get service instance: {}", request);

        final String serviceInstanceId = request.getServiceInstanceId();
        final ServiceInstance serviceInstance = serviceInstanceRepository.findById(serviceInstanceId)
                .orElseThrow(() -> {
                            logger.warn("Couldn't get non existed service instance: {}", request.getServiceInstanceId());
                            return new ServiceInstanceDoesNotExistException(serviceInstanceId);
                        });

        GetServiceInstanceResponse response = GetServiceInstanceResponse.builder()
                .serviceDefinitionId(serviceInstance.getServiceId())
                .planId(serviceInstance.getPlanId())
                .build();

        logger.debug(LOG_MSG_RESPONSE, response);
        return Mono.just(response);
    }

    @Override
    @Transactional
    public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest request) {
        logger.debug("Request to delete service instance: {}", request);

        osbObjectValidationService.validateNotNullServiceDefinition(request.getServiceDefinition(), request.getServiceDefinitionId());
        osbObjectValidationService.validateServicePlan(request.getPlan(), request.getPlanId());

        final String serviceInstanceId = request.getServiceInstanceId();
        final ServiceInstance serviceInstance = serviceInstanceRepository.findById(serviceInstanceId)
                .filter(ins -> ins.getPlanId().equals(request.getPlanId()) && ins.getServiceId().equals(request.getServiceDefinitionId()))
                .orElseThrow( () -> {
                    logger.warn("Couldn't delete non existed service instance: {}", request.getServiceInstanceId());
                    return new ServiceInstanceDoesNotExistException(serviceInstanceId);
                });

        // Delete it from the repository to prohibit addition of new bindings
        logger.info("Try to delete all bindings before deleting instance");
        serviceBindingRepository.deleteServiceBindingsByServiceInstanceId(serviceInstanceId);
        serviceInstanceRepository.deleteById(serviceInstanceId);

        DeleteServiceInstanceResponse response = DeleteServiceInstanceResponse.builder()
                .async(false)
                .build();

        logger.debug(LOG_MSG_RESPONSE, response);
        return Mono.just(response);
    }

    private boolean hasServiceInstanceInfoTheSameParams(final ServiceInstance serviceInstanceInfo, final String serviceDefenitionId, final String planId, final String serviceInstanceId) {
        if (serviceDefenitionId.equals(serviceInstanceInfo.getServiceId()) && planId.equals(serviceInstanceInfo.getPlanId())) {
            return true;
        } else {
            logger.warn("ServiceInstance {} already exists but with different parameters", serviceInstanceId);
            throw new ServiceInstanceExistsException(serviceInstanceId, serviceDefenitionId);
        }
    }
}
