package uz.fluxCrm.fluxCrm.crm.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import uz.fluxCrm.fluxCrm.crm.controller.CompaniesController;
import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.service.CompanyService;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@WebMvcTest(CompaniesController.class)
public class CompaniesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void startUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void getContacts_shouldReturnPagedCompanies() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<CompanyDto> companies = List.of(TestObjectFactory.createCompanyDto());
        Page<CompanyDto> companyPage = new PageImpl<>(companies, pageable, companies.size());

        when(companyService.getCompaniesDto(any(Pageable.class))).thenReturn(companyPage);

        mockMvc.perform(get("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Test Company"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void getContact_shouldReturnCompany() throws Exception {
        CompanyDto companyDto = TestObjectFactory.createCompanyDto();

        when(companyService.findByIdDto(1L)).thenReturn(companyDto);

        mockMvc.perform(get("/companies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Company"));
    }

    @Test
    void createContact_shouldCreateCompany() throws Exception {
        CompanyDto createdCompanyDto = TestObjectFactory.createCompanyDto();

        when(companyService.createDto(any(CompanyDto.class))).thenReturn(createdCompanyDto);

        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdCompanyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Company"));
    }

    @Test
    void updateContact_shouldUpdateCompany() throws Exception {
        CompanyDto updatedCompanyDto = TestObjectFactory.createCompanyDto();

        when(companyService.updateDto(any(CompanyDto.class), anyLong())).thenReturn(updatedCompanyDto);

        mockMvc.perform(patch("/companies/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCompanyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Company"));
    }
}
