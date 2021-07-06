package ru.test.load.serviceTests;

import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static ru.test.load.data.ServiceInstances.*;

public class CreateServiceInstanceTest {

    private Response correctCreatingServiceInstances = createServiceInstances();
    private Response whenInBodyValueOfParamsAreMissing = createServiceInstancesValueOfParamsAreMissing();
    private Response whenServiceDefinitionDoesNotExist = createServiceInstancesDefinitionDoesNotExist();
    private Response whenServicePlanDoesNotExist = createServiceInstancesPlanDoesNotExist();

    @AfterClass
    public void deleteCreatedServiceInstances() {
        deleteServiceInstance().then().statusCode(200);
    }

    @Test(priority = 1)
    public void statusCodeIs201() {
        correctCreatingServiceInstances.then().statusCode(201);
    }

    @Test(priority = 2)
    public void contentTypeIsJson() {
        correctCreatingServiceInstances.then().assertThat().contentType("application/json;charset=UTF-8");
    }

    @Test(priority = 3, description = "Проверка ответа на создание экземпляра модуля с неверным service_id через jsonSchema")
    public void whenServiceDefinitionDoesNotExistJsonSchemaIsValid() {
        whenServiceDefinitionDoesNotExist.then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/badResponse.json"));
    }

    @Test(priority = 3, description = "Проверка ответа на создание экземпляра модуля с неверным service_id")
    public void whenServiceDefinitionDoesNotExist() {
        whenServiceDefinitionDoesNotExist.then().body("description", hasToString("Service definition does not exist: id=1"));
    }

    @Test(priority = 4, description = "Проверка ответа на создание экземпляра модуля с неверным plan_id через jsonSchema")
    public void whenServicePlanDoesNotExistJsonSchemaIsValid() {
        whenServicePlanDoesNotExist.then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/badResponse.json"));
    }

    @Test(priority = 4, description = "Проверка ответа на создание экземпляра модуля с неверным plan_id")
    public void whenServicePlanDoesNotExist() {
        whenServicePlanDoesNotExist.then().body("description", hasToString("Service broker parameters are invalid: Service plan doesn't exist. Plan id: 1"));
    }

    @Test(priority = 5, description = "Проверка ответа на создание экземпляра модуля с неуказанными service_id и plan_id через jsonSchema")
    public void whenInBodyValueOfParamsAreMissingJsonSchemaIsValid() {
        whenInBodyValueOfParamsAreMissing.then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/badResponse.json"));
    }

    @Test(priority = 6, description = "Проверка тела ответа на создание экземпляра модуля с неуказанными service_id и plan_id")
    public void whenValueOfParamsAreMissingBodyResponseIsValid() {
        //  отдельные проверки по подстрокам потому что в ответе serviceDefinitionId и planId выводятся рандомно
        whenInBodyValueOfParamsAreMissing.then().body("description", stringContainsInOrder("Missing required fields:"));
        whenInBodyValueOfParamsAreMissing.then().body("description", stringContainsInOrder("serviceDefinitionId"));
        whenInBodyValueOfParamsAreMissing.then().body("description", stringContainsInOrder("planId"));
    }


}
