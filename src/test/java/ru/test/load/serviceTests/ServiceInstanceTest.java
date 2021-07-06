package ru.test.load.serviceTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static ru.test.load.data.ServiceInstances.*;


public class ServiceInstanceTest {

    @BeforeClass
    public void createServiceInstance() {
        createServiceInstances().then().statusCode(201);
    }

    @AfterClass
    public void deleteCreatedServiceInstances() {
        deleteServiceInstance().then().statusCode(200);
    }

    @Test(priority = 1, description = "Получение ссылки на экземпляр модуля с корректными параметрами")
    public void statusCodeIs200() {
        getLinkService().then().statusCode(200);
    }

    @Test(priority = 2, description = "Получение ссылки на экземпляр модуля с корректными параметрами")
    public void contentTypeIsJson() {
        getLinkService().then().assertThat().contentType("application/json;charset=UTF-8");
    }

    @Test(priority = 3, description = "Проверка успешного ответа на запрос получения ссылки на экземпляр модуля через jsonSchema")
    public void jsonSchemaForSuccessResponseIsValid() {
        getLinkService().then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/successResponseOfLinkService.json"));
    }

    @Test(priority = 4, description = "Сверяем с эталоном тело успешного ответа на запрос получения ссылки на экземпляр модуля")
    public void bodyOfSuccessResponseIsValid() throws IOException {
        JsonPath expectedJson = JsonPath.from(new File("./src/test/resources/expectedJson/serviceInstance/successGetLinkServiceResponse.json"));
        String def = getLinkService().then().extract().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.readTree(expectedJson.prettify());
        JsonNode actual = mapper.readTree(def);
        Assert.assertEquals(actual, expected);
    }

}
