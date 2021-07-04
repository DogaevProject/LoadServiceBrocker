package ru.test.load.repository.instance;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.test.load.service.ServiceInstance;

public interface DatabaseServiceInstanceRepository<T extends ServiceInstance> extends CrudRepository<T, String> {
    @Query("select s.serviceInstanceId from ServiceInstance as s")
    List<String> getServiceInstanceIdList();

    List<T> findAll();
}

