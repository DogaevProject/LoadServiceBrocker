package ru.test.load.repository;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.test.load.service.ServiceBinding;
import ru.test.load.service.ServiceInstance;

@Configuration
@AutoConfigureAfter({JpaRepositoriesAutoConfiguration.class})
@EnableJpaRepositories
@EntityScan(
        basePackageClasses = {ServiceBinding.class, ServiceInstance.class},
        basePackages = {"ru.test.load"}
)
public class OsbPersistenceAutoConfiguration {
    public OsbPersistenceAutoConfiguration() {
    }
}
