package com.epam.mentoring.webservices.tests;


import com.epam.mentoring.webservices.model.User;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.regex.Pattern;

import static com.epam.mentoring.webservices.help.TestResource.getTestResourceAsFile;
import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class RestAssuredTest {


    private static final URI JSON_SCHEMA_FOR_GET_RESPONSE = getTestResourceAsFile("schema.json").toURI();

    private String[] names = { "Leanne Graham", "Ervin Howell", "Clementine Bauch", "Patricia Lebsack", "Chelsey Dietrich",
                               "Mrs. Dennis Schulist", "Kurtis Weissnat", "Nicholas Runolfsdottir V", "Glenna Reichert",
                               "Clementina DuBuque" };

    @BeforeTest
    public void initTest () {
        RestAssured.config = RestAssuredConfig.config()
                                              .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(ALL))
                                              .sslConfig(SSLConfig.sslConfig().allowAllHostnames());
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

    }

    @Test
    public void checkStatusCode () {
        Response response = RestAssured.when().get("/users").andReturn();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void checkResponseHeader () {
        Response response = RestAssured.when().get("/users").andReturn();

        String rpContentTypeHeader = response.getHeader("Content-Type");
        Assert.assertEquals(rpContentTypeHeader, "application/json; charset=utf-8");
    }

    @Test
    public void CheckResponseBody () {
        Response response = RestAssured.when().get("/users")

                                       .andReturn();
        ResponseBody<?> responseBody = response.getBody();
        User[] users = responseBody.as(User[].class);
        Assert.assertEquals(users.length, 10);
    }

    @Test
    public void CheckNameResponseBody () {
        RestAssured.defaultParser = Parser.JSON;
        ValidatableResponse response = given().when()
                                              .log()
                                              .everything()
                                              .get("/users")
                                              .then()
                                              .log()
                                              .everything()
                                              .assertThat()
                                              .body(matchesJsonSchema(JSON_SCHEMA_FOR_GET_RESPONSE))
                                              .statusCode(200)
                                              .header("Content-Type", "application/json; charset=utf-8")
                                              .body("name", Matchers.anyOf(Matchers.contains(names)))
                                              .body("$.email", Matchers.everyItem(Matchers.matchesPattern(Pattern.compile("@"))));

    }
}
