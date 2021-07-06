package ru.test.load.data;

import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class ServiceInstances extends BaseClass {

    /**
     * Создание экземпляра модуля с корректными параметрами
     *
     * @return успешный Response
     */
    public static Response createServiceInstances() {
        return given()
                .contentType("application/json")
                .header("X-Broker-API-Version", "2.13")
                .body(new File("./src/test/resources/request/createServiceInstances.json"))
                .when()
                .put(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b");
    }

    /**
     * Создание экземпляра модуля с некорректными параметрами (Missing required fields: planId serviceDefinitionId )
     *
     * @return Response с ошибкой
     */
    public static Response createServiceInstancesValueOfParamsAreMissing() {
        return given()
                .contentType("application/json")
                .header("X-Broker-API-Version", "2.13")
                .body(new File("./src/test/resources/request/serviceIdAndPlanIdAreMissing.json"))
                .when()
                .put(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b");
    }

    /**
     * Создание экземпляра модуля с некорректными параметрами (Service definition does not exist)
     *
     * @return Response с ошибкой
     */
    public static Response createServiceInstancesDefinitionDoesNotExist() {
        return given()
                .contentType("application/json")
                .header("X-Broker-API-Version", "2.13")
                .body(new File("./src/test/resources/request/serviceDefinitionDoesNotExist.json"))
                .when()
                .put(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b");
    }

    /**
     * Создание экземпляра модуля с некорректными параметрами (Service plan doesn't exist)
     *
     * @return Response с ошибкой
     */
    public static Response createServiceInstancesPlanDoesNotExist() {
        return given()
                .contentType("application/json")
                .header("X-Broker-API-Version", "2.13")
                .body(new File("./src/test/resources/request/servicePlanDoesNotExist.json"))
                .when()
                .put(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b");
    }


    /**
     * Получение ссылки на экземпляр модуля с корректными параметрами
     *
     * @return успешный Response
     */
    public static Response getLinkService() {
        return given()
                .header("X-Broker-API-Version", "2.13")
                .when()
                .get(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b");
    }


    /**
     * Удаление существующего экземпляра модуля
     */
    public static Response deleteServiceInstance() {
        return given()
                .header("X-Broker-API-Version", "2.13")
                .when()
                .delete(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b?service_id=acdd7cfc-ccd9-11ea-87d0-0242ac130005&plan_id=acdd7cfc-ccd9-11ea-87d0-0242ac130005");
    }

    /**
     * Удаление несуществующего экземпляра модуля - некорректно указан Plan id
     */
    public static Response deleteInstanceWithPlanDoesNotExist() {
        return given()
                .header("X-Broker-API-Version", "2.13")
                .when()
                .delete(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b?service_id=acdd7cfc-ccd9-11ea-87d0-0242ac130005&plan_id=1");
    }

    /**
     * Удаление несуществующего экземпляра модуля - некорректно указан service_id
     */
    public static Response deleteInstanceWithServiceDefinitionDoesNotExist() {
        return given()
                .header("X-Broker-API-Version", "2.13")
                .when()
                .delete(baseURI + "/service_instances/322574a5-d25f-4035-8277-7db289951f3b?service_id=1&plan_id=acdd7cfc-ccd9-11ea-87d0-0242ac130005");
    }


}
