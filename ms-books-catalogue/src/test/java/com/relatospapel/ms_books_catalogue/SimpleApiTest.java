package com.relatospapel.ms_books_catalogue;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

/**
 * Test simple para diagnosticar problemas con REST Assured
 */
public class SimpleApiTest {

    @BeforeAll
    @SuppressWarnings("unused")
    static void setup() {
        RestAssured.baseURI = "http://localhost:8081";
    }

    @Test
    void testCreateCategory() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        Map<String, Object> category = new HashMap<>();
        category.put("name", "Simple Test " + timestamp);
        category.put("description", "Test description");
        
        String id = given()
            .contentType(ContentType.JSON)
            .body(category)
            .log().all()
        .when()
            .post("/api/v1/catalogue/categories")
        .then()
            .log().all()
            .statusCode(201)
            .body("name", equalTo("Simple Test " + timestamp))
            .body("id", notNullValue())
        .extract()
            .path("id");
        
        System.out.println("Created category ID: " + id);
    }
}
