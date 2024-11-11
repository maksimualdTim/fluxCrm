package uz.fluxCrm.fluxCrm.crm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;
import uz.fluxCrm.fluxCrm.crm.service.DefaultLeadService;

@WebMvcTest(LeadsController.class)
public class LeadsControllerTest {

    @MockBean
    DefaultLeadService leadService;

    @MockBean
    StatusRepository statusRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(statusRepository.existsById(1L)).thenReturn(true);
    }

    @Test
    @Description("Test method POST /leads. Should return 201.")
    void createLead_ValidLead_ReturnsCreatedResponse() throws Exception {
        LeadDto leadDto = new LeadDto();
        leadDto.setName("Test Lead");
        leadDto.setPrice(1000L);
        leadDto.setPipelineId(1L);
        leadDto.setStatusId(1L);  // Предполагаем, что статус с ID 1 существует в базе

        // Предположим, что сервис возвращает тот же объект, что и был передан
        when(leadService.createLeadDto(any(LeadDto.class))).thenReturn(leadDto);

        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Lead\", \"price\":1000, \"pipeline_id\":1, \"status_id\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Lead"))
                .andExpect(jsonPath("$.price").value(1000L))
                .andExpect(jsonPath("$.pipeline_id").value(1L))
                .andExpect(jsonPath("$.status_id").value(1L));
    }

    @Test
    @Description("Test method POST /leads. Should return 400.")
    void createLead_InvalidStatus_ReturnsBadRequest() throws Exception {
        LeadDto leadDto = new LeadDto();
        leadDto.setName("Test Lead");
        leadDto.setPrice(1000L);
        leadDto.setPipelineId(1L);
        leadDto.setStatusId(-999L);

        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Lead\", \"price\":1000, \"pipeline_id\":1, \"status_id\":999}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[0].field").value("status_id"))
                .andExpect(jsonPath("$.errors[0].message").value("Status does not exists"));
    }



    @Test
    @Description("Test method GET /leads/{id}. Should return 404.")
    void getLead_LeadNotFound_ReturnsNotFoundResponse() throws Exception {
        when(leadService.findById(-1L)).thenThrow(new EntityNotFoundException("Lead not found by ID: -1"));

        mockMvc.perform(get("/leads/{id}", -1L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value("Lead not found by ID: " + -1));
    }

    @Test
    @Description("Test method /leads/{id}. Should return 200.")
    void getLead_LeadFound_ReturnsOkResponse() throws Exception {
        LeadDto leadDto = new LeadDto();
        leadDto.setId(1L);
        leadDto.setName("Test lead");
        leadDto.setPipelineId(1L);
        leadDto.setPrice(0L);
        leadDto.setStatusId(2L);

        when(leadService.findByIdDto(1L)).thenReturn(leadDto);

        mockMvc.perform(get("/leads/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Test lead"))
            .andExpect(jsonPath("$.pipeline_id").value(1L))
            .andExpect(jsonPath("$.price").value(0L))
            .andExpect(jsonPath("$.status_id").value(2L));
    }

    @Test
    void testGetLeads() {

    }
}
