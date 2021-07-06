package ru.test.load.serviceTests;

import org.testng.annotations.Test;

import static org.hamcrest.Matchers.hasToString;
import static ru.test.load.data.ServiceInstances.*;

public class DeleteServiceInstanceTest {

    @Test(priority = 1, description = "Удаление существующего экземпляра модуля")
    public void deletingServiceInstance() {
        // Создаем модуль
        createServiceInstances().then().statusCode(201);
        // Удаляем модуль
        deleteServiceInstance().then().statusCode(200);
        // Проверяем что модуль удален
        getLinkService().then().statusCode(404);
    }

    @Test(priority = 2, description = "Попытка удаления экземпляра модуля с некорректно указанным Plan id")
    public void deletingServiceWithPlanDoesNotExist() {
        deleteInstanceWithPlanDoesNotExist().then().body("description", hasToString("Service broker parameters are invalid: Service plan doesn't exist. Plan id: 1"));
    }


    @Test(priority = 2, description = "Попытка удаления экземпляра модуля с некорректно указанным Service definition")
    public void deletingWithServiceDefinitionDoesNotExist() {
        deleteInstanceWithServiceDefinitionDoesNotExist().then().body("description", hasToString("Service definition does not exist: id=1"));
    }

}
