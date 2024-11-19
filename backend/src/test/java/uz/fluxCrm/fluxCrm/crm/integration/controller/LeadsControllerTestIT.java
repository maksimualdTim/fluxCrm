package uz.fluxCrm.fluxCrm.crm.integration.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import java.util.List;

import uz.fluxCrm.fluxCrm.crm.dto.CompanyDtoSimple;
import uz.fluxCrm.fluxCrm.crm.dto.ContactDtoSimple;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.integration.BaseIntegrationTest;
import uz.fluxCrm.fluxCrm.crm.service.DefaultLeadService;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;


public class LeadsControllerTestIT extends BaseIntegrationTest{

    @Autowired
    DefaultLeadService leadService;


    @Test
    void shouldCreateLeadReturnsLeadDto() throws JsonProcessingException{
        LeadDto leadDto = TestObjectFactory.createTestLead();
        CompanyDtoSimple companyDto = TestObjectFactory.createCompanyDtoSimple();
        ContactDtoSimple contactDto = TestObjectFactory.createContactDtoSimple();

        leadDto.setCompany(companyDto);
        leadDto.setContacts(List.of(contactDto));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(leadDto))
        .when()
            .post("/leads")
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", Matchers.any(Integer.class))
            .body("name", Matchers.equalTo("Test Lead"))
            .body("price", Matchers.equalTo(1000))
            .body("contacts", Matchers.hasSize(1))
            .body("contacts[0].id", Matchers.any(Integer.class))
            .body("contacts[0].name", Matchers.equalTo("Test Contact"))
            .body("company.id", Matchers.any(Integer.class))
            .body("company.name", Matchers.equalTo("Test Company"));
    }

    @Test
    void shouldReturnLeadDto() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads/{id}", 2L)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", Matchers.equalTo(2))
            .body("name", Matchers.equalTo("Test Lead"))
            .body("price", Matchers.equalTo(1000))
            .body("contacts", Matchers.hasSize(1))
            .body("contacts[0].id", Matchers.equalTo(1))
            .body("contacts[0].name", Matchers.equalTo("Test Contact"))
            .body("company.id", Matchers.equalTo(1))
            .body("company.name", Matchers.equalTo("Test Company"));
    }

    @Test
    void shouldThrowNotFoundOnGetLead() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads/{id}", -1L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Lead not found by ID: -1"));

    }

    @Test
    void shouldReturnLeadDtoPage() {
        LeadDto leadDto = TestObjectFactory.createTestLead();
        CompanyDtoSimple companyDto = TestObjectFactory.createCompanyDtoSimple();
        ContactDtoSimple contactDto = TestObjectFactory.createContactDtoSimple();

        leadDto.setCompany(companyDto);
        leadDto.setContacts(List.of(contactDto));

        leadService.createLead(leadDto);

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("content", Matchers.hasSize(1))
            .body("content[0].id", Matchers.equalTo(2))
            .body("content[0].name", Matchers.equalTo("Test Lead"))
            .body("totalElements", Matchers.equalTo(1))
            .body("totalPages", Matchers.equalTo(1));
    }

    @Test
    void shouldDeleteLead() {
        LeadDto leadDto = TestObjectFactory.createTestLead();
        CompanyDtoSimple companyDto = TestObjectFactory.createCompanyDtoSimple();
        ContactDtoSimple contactDto = TestObjectFactory.createContactDtoSimple();

        leadDto.setCompany(companyDto);
        leadDto.setContacts(List.of(contactDto));

        leadService.createLead(leadDto);

        given()
        .when()
            .delete("/leads/{id}", 1L)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        get("/leads/{id}", 1L).then()
        .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldCreatePipelineReturnsPipelineDto() throws JsonProcessingException{
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("Test Pipeline");

        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pipelineDto))
            .when()
            .post("/leads/pipelines")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", Matchers.equalTo(2))
            .body("name", Matchers.equalTo("Test Pipeline"))
            .body("statuses",Matchers.hasSize(6))
            .body("statuses.name", Matchers.containsInAnyOrder(
                "Первичный контак", 
                "Отправили договор", 
                "Переговоры", 
                "Принимают решение", 
                "Успешно реализовано", 
                "Закрыто и не реализовано"
            ));
    }

    @Test
    void shouldThrowValidationExceptionOnCreatePipeline() throws JsonProcessingException{
        PipelineDto pipelineDto = new PipelineDto();
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pipelineDto))
            .when()
            .post("/leads/pipelines")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("status", Matchers.equalTo(HttpStatus.BAD_REQUEST.value()))
            .body("errors", Matchers.hasSize(1))
            .body("errors[0].field", Matchers.equalTo("name"))
            .body("errors[0].message", Matchers.equalTo("must not be blank"));
    }

    @Test
    void shouldGetDefaultPipelineDto() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/leads/pipelines/{id}", 1L)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", Matchers.equalTo(1))
            .body("name", Matchers.equalTo("Воронка"))
            .body("statuses",Matchers.hasSize(6))
            .body("statuses.name", Matchers.containsInAnyOrder(
                "Первичный контак", 
                "Отправили договор", 
                "Переговоры", 
                "Принимают решение", 
                "Успешно реализовано", 
                "Закрыто и не реализовано"
            ));
    }

    @Test
    void shouldThrowPipelineNotFoundExceptionOnGetPipeline() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/leads/pipelines/{id}", -1L)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Pipeline not found by ID: -1"));
    }

    @Test
    void shouldUpdatePipelineReturnsPipelineDto() throws JsonProcessingException{
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("TestNameUpdated");
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pipelineDto))
        .when()
            .patch("/leads/pipelines/{id}", 1L)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", Matchers.equalTo("TestNameUpdated"))
            .body("id", Matchers.equalTo(1))
            .body("statuses",Matchers.hasSize(6))
            .body("statuses.name", Matchers.containsInAnyOrder(
                "Первичный контак", 
                "Отправили договор", 
                "Переговоры", 
                "Принимают решение", 
                "Успешно реализовано", 
                "Закрыто и не реализовано"
            ));
    }

    @Test
    void shouldDeletePipeline() {
        given()
        .when()
            .delete("/leads/pipelines/{id}", 2L)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        get("/leads/pipelines/{id}", 2L).then()
        .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldThrowValidationErrorExceptionOnUpdate() throws JsonProcessingException{
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("");
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pipelineDto))
        .when()
            .patch("/leads/pipelines/{id}", 2L)
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("status", Matchers.equalTo(HttpStatus.BAD_REQUEST.value()))
            .body("errors", Matchers.hasSize(1))
            .body("errors[0].field", Matchers.equalTo("name"))
            .body("errors[0].message", Matchers.equalTo("must not be blank"));
    }

    @Test
    void shouldThrowNotFoundExceptionOnUpdate() throws JsonProcessingException{
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("Test");
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(pipelineDto))
        .when()
            .patch("/leads/pipelines/{id}", -2L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Pipeline not found by ID: -2"));
    }

    @Test
    void shouldGetStatusDtoList() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads/pipelines/{id}/statuses", 1L)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body(".", Matchers.hasSize(6))
            .body("name", Matchers.containsInAnyOrder(
                "Первичный контак", 
                "Отправили договор", 
                "Переговоры", 
                "Принимают решение", 
                "Успешно реализовано", 
                "Закрыто и не реализовано"
            ));
    }

    @Test
    void shouldGetStatusDto() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads/pipelines/{pipelineId}/statuses/{id}", 1L, 1L)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", Matchers.equalTo(1))
            .body("name", Matchers.equalTo("Первичный контак"))
            .body("pipeline_id", Matchers.equalTo(1));
    }

    @Test
    void shouldThrowNotFoundPipelineExceptionOnStatusGet() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads/pipelines/{pipelineId}/statuses/{id}", -1L, 1L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Pipeline not found by ID: -1"));
    }

    @Test
    void shouldThrowNotFoundStatusExceptionOnStatusGet() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/leads/pipelines/{pipelineId}/statuses/{id}", 1L, -1L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Status not found by ID: -1"));
    }

    @Test
    void shouldCreateStatusReturnsStatusDto() throws JsonProcessingException{
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .post("/leads/pipelines/{id}/statuses", 1L)
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("id", Matchers.any(Integer.class))
            .body("name", Matchers.equalTo("Test Status"))
            .body("pipeline_id", Matchers.equalTo(1));
    }

    @Test
    void shouldThrowNotFoundExceptionOnStatusCreate() throws JsonProcessingException{
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .post("/leads/pipelines/{id}/statuses", -1L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Pipeline not found by ID: -1"));
    }

    @Test
    void shouldThrowBadRequestExceptionOnStatusCreate() throws JsonProcessingException{
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        statusDto.setName(null);
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .post("/leads/pipelines/{id}/statuses", 1L)
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("status", Matchers.equalTo(HttpStatus.BAD_REQUEST.value()))
            .body("errors", Matchers.hasSize(1))
            .body("errors[0].field", Matchers.equalTo("name"))
            .body("errors[0].message", Matchers.equalTo("must not be blank"));
    }
      
    @Test
    void shouldUpdateStatusReturnsStatusDto() throws JsonProcessingException {
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", Matchers.equalTo(1))
            .body("name", Matchers.equalTo("Test Status"))
            .body("pipeline_id", Matchers.equalTo(1));
    }

    @Test
    void shouldThrowStatusNotFoundExceptionOnStatusUpdate() throws JsonProcessingException {
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, -1L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Status not found by ID: -1"));
    }

    @Test
    void shouldThrowPipelineNotFoundExceptionOnStatusUpdate() throws JsonProcessingException {
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", -1L, 1L)
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", Matchers.equalTo(HttpStatus.NOT_FOUND.value()))
            .body("message", Matchers.equalTo("Pipeline not found by ID: -1"));
    }

    @Test
    void shouldThrowBadRequestExceptionOnStatusUpdate() throws JsonProcessingException{
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        statusDto.setName(null);
        ObjectMapper objectMapper = new ObjectMapper();

        given()
            .contentType(ContentType.JSON)
            .body(objectMapper.writeValueAsString(statusDto))
        .when()
            .patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("status", Matchers.equalTo(HttpStatus.BAD_REQUEST.value()))
            .body("errors", Matchers.hasSize(1))
            .body("errors[0].field", Matchers.equalTo("name"))
            .body("errors[0].message", Matchers.equalTo("must not be blank"));
    }

    @Test
    void shouldDeleteStatus() {
        given()
        .when()
            .delete("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 2L)
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        get("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 2L)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
