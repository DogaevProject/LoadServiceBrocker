package ru.test.load.repository.instance;
import org.springframework.stereotype.Repository;
import ru.test.load.service.ServiceInstance;

@Repository
public interface CustomServiceInstanceRepository<T extends ServiceInstance> {
    T putIfAbsent(T serviceInstance);
}
