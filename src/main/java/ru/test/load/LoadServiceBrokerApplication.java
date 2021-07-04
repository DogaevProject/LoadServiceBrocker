package ru.test.load;

import ru.test.load.config.ServiceBrokerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ServiceBrokerConfiguration.class})
@EntityScan(basePackages = "ru.test.load")
public class LoadServiceBrokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadServiceBrokerApplication.class, args);
    }
}

