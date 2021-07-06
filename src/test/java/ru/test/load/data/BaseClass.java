package ru.test.load.data;


import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;


public class BaseClass {

    static final String baseURI = RestAssured.baseURI + ":8082/v2";

}
