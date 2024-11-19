package uz.fluxCrm.fluxCrm.crm.integration.controller;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import uz.fluxCrm.fluxCrm.crm.integration.BaseIntegrationTest;

public class ContactsControllerTestIT extends BaseIntegrationTest{

    @Test
    void shouldThrowsNotFoundException() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/leads/pipelines/{id}", 1L)
            .then()
            .statusCode(200);
    }
}
