package ru.test.load.catalogTests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static ru.test.load.data.Catalog.*;


public class CatalogTest {

    private Response correctResponseOfCatalog = getCatalog(); //  Получение информации о модуле с корректными параметрами в запросе
    private Response whenRequestNotContainsAPIVersion = getCatalogRequestNotContainsAPIVersion(); // Получение информации о модуле с некорректными параметрами в запросе (в header отсутвует версия API)


    @Test(priority = 1)
    public void statusCodeIs200() {
        correctResponseOfCatalog.then().statusCode(200);
    }

    @Test(priority = 2)
    public void contentTypeIsJson() {
        correctResponseOfCatalog.then().assertThat().contentType("application/json;charset=UTF-8");
    }

    @Test(priority = 3, description = "Проверка успешного ответа на получение информации о модуле через jsonSchema")
    public void jsonSchemaForSuccessResponseIsValid() {
        correctResponseOfCatalog.then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/successResponseOfCatalogSchema.json"));
    }

    @Test(priority = 4, description = "Сверяем с эталоном тело успешного ответа на получение информации о модуле")
    public void bodyOfSuccessResponseIsValid() throws IOException {
        JsonPath expectedJson = JsonPath.from(new File("./src/test/resources/expectedJson/catalog/successCatalogResponse.json"));
        String def = correctResponseOfCatalog.then().extract().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.readTree(expectedJson.prettify());
        JsonNode actual = mapper.readTree(def);
        Assert.assertEquals(actual, expected);
    }


    @Test(priority = 5, description = "Проверка ответа с ошибкой (в header отсутвует версия API) на получение информации о модуле")
    public void statusCodeIs400() {
        whenRequestNotContainsAPIVersion.then().statusCode(400);
    }

    @Test(priority = 6, description = "Проверка ответа с ошибкой (в header отсутвует версия API) на получение информации о модуле через jsonSchema")
    public void whenRequestNotContainsAPIVersionJsonSchemaIsValid() {
        whenRequestNotContainsAPIVersion.then().assertThat().body(matchesJsonSchemaInClasspath("jsonSchema/badResponse.json"));
    }


    @Test(priority = 7, description = "Сверяем с эталоном тело ответа с ошибкой (в header отсутвует версия API) на получение информации о модуле")
    public void whenRequestNotContainsAPIVersionBodyResponseIsValid() throws IOException {
        JsonPath expectedJson = JsonPath.from(new File("./src/test/resources/expectedJson/catalog/whenRequestNotContainsAPIVersion.json"));
        String def = whenRequestNotContainsAPIVersion.then().extract().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.readTree(expectedJson.prettify());
        JsonNode actual = mapper.readTree(def);
        Assert.assertEquals(actual, expected);
    }


}
