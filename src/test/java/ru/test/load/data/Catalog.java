package ru.test.load.data;

import io.restassured.response.Response;
import ru.test.load.data.BaseClass;

import static io.restassured.RestAssured.given;

public class Catalog extends BaseClass {


    /**
     * Получение информации о модуле с корректными параметрами
     *
     * @return успешный Response
     */
    public static Response getCatalog() {
        return given()
                .header("X-Broker-API-Version", "2.13")
                .when()
                .get(baseURI + "/catalog");
    }


    /**
     * Получение информации о модуле с некорректными параметрами (в header отсутвует версия API)
     *
     * @return Response с ошибкой
     */
    public static Response getCatalogRequestNotContainsAPIVersion() {
        return given()
                .when()
                .get(baseURI + "/catalog");
    }
}
