package ru.test.load.repository.binding;


import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.test.load.serviceTests.ServiceBinding;

public interface DatabaseServiceBindingRepository<T extends ServiceBinding> extends CrudRepository<T, String> {
    List<T> findAll();

    @Query("select b.serviceBindingId from ServiceBinding as b")
    List<String> getServiceBindingIdList();

    void deleteServiceBindingsByServiceInstanceId(String serviceInstanceId);

    List<T> getServiceBindingsByServiceInstanceId(String serviceInstanceId);
}

