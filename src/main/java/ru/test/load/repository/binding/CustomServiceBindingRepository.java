package ru.test.load.repository.binding;

import org.springframework.stereotype.Repository;
import ru.test.load.serviceTests.ServiceBinding;

@Repository
public interface CustomServiceBindingRepository<T extends ServiceBinding> {
    T putIfAbsent(T serviceBinding);
}
