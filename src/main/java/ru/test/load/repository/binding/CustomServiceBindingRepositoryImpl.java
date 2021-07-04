package ru.test.load.repository.binding;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.test.load.service.ServiceBinding;

@Repository
public class CustomServiceBindingRepositoryImpl<T extends ServiceBinding> implements CustomServiceBindingRepository<T> {
    private final DatabaseServiceBindingRepository<T> databaseServiceBindingRepository;

    @Autowired
    public CustomServiceBindingRepositoryImpl(DatabaseServiceBindingRepository<T> databaseServiceBindingRepository) {
        this.databaseServiceBindingRepository = databaseServiceBindingRepository;
    }

    @Transactional
    public T putIfAbsent(T serviceBinding) {
        String bindingId = serviceBinding.getServiceBindingId();
        Optional<T> optionalBinding = this.databaseServiceBindingRepository.findById(bindingId);
        if (optionalBinding.isPresent()) {
            return optionalBinding.get();
//            return (ServiceBinding)optionalBinding.get();
        } else {
            this.databaseServiceBindingRepository.save(serviceBinding);
            return null;
        }
    }
}
