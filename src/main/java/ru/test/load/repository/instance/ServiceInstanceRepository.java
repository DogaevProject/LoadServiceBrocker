package ru.test.load.repository.instance;

import org.springframework.stereotype.Repository;
import ru.test.load.service.ServiceInstance;

@Repository
public interface ServiceInstanceRepository<T extends ServiceInstance> extends CustomServiceInstanceRepository<T>, DatabaseServiceInstanceRepository<T> {
}

