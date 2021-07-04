package ru.test.load.service;

import ru.test.load.config.ServiceBrokerConfiguration;
import ru.test.load.loginmanager.LoginManager;
import ru.test.load.loginmanager.RequestInfo;
import ru.test.load.loginmanager.UserInfoResponse;
import ru.test.load.model.LoadServiceBinding;
import ru.test.load.repository.binding.ServiceBindingRepository;
import ru.test.load.repository.instance.ServiceInstanceRepository;
import ru.test.load.validation.OsbObjectValidationService;
import ru.test.load.validation.UuidValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.model.binding.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoadBindingService implements ServiceInstanceBindingService {


    private static final String LOG_MSG_RESPONSE = "Response is going to be sent: {}";

    private static final Logger logger = LoggerFactory.getLogger(LoadBindingService.class);
    private final OsbObjectValidationService osbObjectValidationService;
    private final ServiceInstanceRepository<ServiceInstance> serviceInstanceRepository;
    private final ServiceBindingRepository<LoadServiceBinding> serviceBindingRepository;
    private final ServiceBrokerConfiguration brokerConfig;
    private final LoginManager loginManager;

    public LoadBindingService(OsbObjectValidationService osbObjectValidationService, ServiceInstanceRepository<ServiceInstance> serviceInstanceRepository, ServiceBindingRepository<LoadServiceBinding> serviceBindingRepository, ServiceBrokerConfiguration brokerConfig, LoginManager loginManager) {
        this.osbObjectValidationService = osbObjectValidationService;
        this.serviceInstanceRepository = serviceInstanceRepository;
        this.serviceBindingRepository = serviceBindingRepository;
        this.brokerConfig = brokerConfig;
        this.loginManager = loginManager;
    }

    @Override
    public Mono<CreateServiceInstanceBindingResponse> createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        logger.debug("Request to create service instance binding: {}", request);

        osbObjectValidationService.validateNotNullServiceDefinition(request.getServiceDefinition(), request.getServiceDefinitionId());
        osbObjectValidationService.validateServicePlan(request.getPlan(), request.getPlanId());

        if (!serviceBindingRepository.getServiceBindingsByServiceInstanceId(request.getServiceInstanceId()).isEmpty()) {
            throw new UnsupportedOperationException("Service binding for service instance already exist");
        }

        final String serviceInstanceId = request.getServiceInstanceId();
        CreateServiceInstanceBindingResponse response = createUserAndPrepareResponse(request.getServiceDefinitionId(), request.getPlanId(), serviceInstanceId, request.getBindingId());

        logger.debug(LOG_MSG_RESPONSE, response);
        return Mono.just(response);
    }

    @Override
    public Mono<GetServiceInstanceBindingResponse> getServiceInstanceBinding(GetServiceInstanceBindingRequest request) {
        logger.debug("Request to get service instance binding: {}", request);
        String serviceInstanceId = request.getServiceInstanceId();
        ServiceInstance serviceInstance = getServiceInstanceInfoOrThrowException(serviceInstanceId);
        logger.info("Requested instance id {} exist", serviceInstance.getServiceInstanceId());

        LoadServiceBinding serviceBinding = serviceBindingRepository
                .findById(request.getBindingId())
                .orElseThrow(() -> {
                    logger.warn("ServiceBinding with id {} does not exist", request.getBindingId());
                    return new ServiceInstanceBindingDoesNotExistException(request.getBindingId());
                });

        GetServiceInstanceBindingResponse response = GetServiceInstanceAppBindingResponse.builder()
                .credentials("USER_NAME", serviceBinding.getUserName())
                .credentials("USER_PASSWORD", serviceBinding.getUserPassword())
                .credentials("URI", serviceBinding.getURI())
                .build();

        logger.debug(LOG_MSG_RESPONSE, response);
        return Mono.just(response);
    }

    @Override
    public Mono<DeleteServiceInstanceBindingResponse> deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
        logger.debug("Request to delete service instance binding: {}", request);

        osbObjectValidationService.validateNotNullServiceDefinition(request.getServiceDefinition(), request.getServiceDefinitionId());
        osbObjectValidationService.validateServicePlan(request.getPlan(), request.getPlanId());

        final String serviceInstanceId = request.getServiceInstanceId();
        final String bindingId = request.getBindingId();

        final ServiceInstance serviceInstance = serviceInstanceRepository.findById(serviceInstanceId).orElseThrow(() -> {
                logger.warn("Service instance {} does not exist", serviceInstanceId);
                return new ServiceInstanceDoesNotExistException(serviceInstanceId);
            });

        LoadServiceBinding binding = serviceBindingRepository.findById(bindingId).orElse(null);

        if (binding == null) {
            logger.warn("Service instance binding {} does not exist", bindingId);
            throw new ServiceInstanceBindingDoesNotExistException(bindingId);
        } else {
            if (    serviceInstance.getServiceInstanceId().equals(serviceInstanceId)    &&
                    serviceInstance.getPlanId().equals(request.getPlanId())             &&
                    serviceInstance.getServiceId().equals(request.getServiceDefinitionId())) {
                boolean b = loginManager.deleteUser(new RequestInfo("Login", "SolCode"));
                if (!b) logger.error("Problems with deleting user");
                serviceBindingRepository.deleteById(bindingId);
            } else {
                logger.warn("ServiceInstanceBinding {} already exists but with different parameters", bindingId);
                throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
            }
        }

        DeleteServiceInstanceBindingResponse response = DeleteServiceInstanceBindingResponse.builder()
                .async(false)
                .build();

        logger.debug(LOG_MSG_RESPONSE, response);

        return Mono.just(response);
    }

    private CreateServiceInstanceBindingResponse createUserAndPrepareResponse(final String serviceId, final String planId, final String serviceInstanceId, final String bindingId) {
        if (brokerConfig.isUuidValidationEnabled()) {
            UuidValidator.validateUuidOrThrowException(bindingId, "BindingId", logger);
        }

        CreateServiceInstanceAppBindingResponse.CreateServiceInstanceAppBindingResponseBuilder responseBuilder =
                CreateServiceInstanceAppBindingResponse.builder().async(false);

        final ServiceInstance serviceInstance = getServiceInstanceInfoOrThrowException(serviceInstanceId);

        if (serviceBindingRepository.existsById(bindingId)) {
            responseBuilder.bindingExisted(hasServiceInstanceInfoTheSameParams(serviceInstance, serviceId, planId, serviceInstanceId, bindingId));
        }
        else {
            UserInfoResponse user = loginManager.createUser(new RequestInfo("Login", "SolCode"));
            if (user == null) {
                logger.error("Cannot create a user and grand privileges");
                throw new RuntimeException("Internal Error: A service binding cannot be created");
            }

            LoadServiceBinding binding = new LoadServiceBinding(serviceInstanceId, bindingId, "LoadTesting", user.username, user.password, user.uri);
            serviceBindingRepository.putIfAbsent(binding);
            responseBuilder.bindingExisted(false);

            responseBuilder
                    .credentials("USER_NAME", binding.getUserName())
                    .credentials("USER_PASSWORD", binding.getUserPassword())
                    .credentials("URI", binding.getURI());
        }

        return responseBuilder.build();
    }

    private ServiceInstance getServiceInstanceInfoOrThrowException(final String serviceInstanceId) {
        return serviceInstanceRepository.findById(serviceInstanceId)
                .orElseThrow(() -> {
                    logger.warn("The specified serviceInstance {} does not exist", serviceInstanceId);
                    return new ServiceInstanceDoesNotExistException(serviceInstanceId);
                });
    }

    private boolean hasServiceInstanceInfoTheSameParams(final ServiceInstance serviceInstance, final String serviceId, final String planId, final String serviceInstanceId, final String validatedBindingId) {
        if (serviceId.equals(serviceInstance.getServiceId()) && planId.equals(serviceInstance.getPlanId())) {
            return true;
        } else {
            logger.warn("ServiceInstanceBinding {} already exists but with different parameters", validatedBindingId);
            throw new ServiceInstanceBindingExistsException(serviceInstanceId, validatedBindingId);
        }
    }
}
