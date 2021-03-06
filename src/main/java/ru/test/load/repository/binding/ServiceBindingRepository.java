package ru.test.load.repository.binding;


import org.springframework.stereotype.Repository;
import ru.test.load.service.ServiceBinding;

@Repository
public interface ServiceBindingRepository<T extends ServiceBinding> extends CustomServiceBindingRepository<T>, DatabaseServiceBindingRepository<T> {
}

