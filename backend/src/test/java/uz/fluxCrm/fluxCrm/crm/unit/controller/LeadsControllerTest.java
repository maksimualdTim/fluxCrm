package uz.fluxCrm.fluxCrm.crm.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import uz.fluxCrm.fluxCrm.crm.controller.LeadsController;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;
import uz.fluxCrm.fluxCrm.crm.service.DefaultLeadService;
import uz.fluxCrm.fluxCrm.crm.service.DefaultPipelineService;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@WebMvcTest(LeadsController.class)
public class LeadsControllerTest {

    @MockBean
    DefaultLeadService leadService;

    @MockBean
    DefaultPipelineService pipelineService;

    @MockBean
    StatusRepository statusRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(statusRepository.existsById(1L)).thenReturn(true);
        objectMapper = new ObjectMapper();
    }

    @Test
    @Description("Test method POST /leads. Should return 201.")
    void createLead_ValidLead_ReturnsCreatedResponse() throws Exception {
        LeadDto leadDto = TestObjectFactory.createTestLead();

        when(leadService.createLeadDto(any(LeadDto.class))).thenReturn(leadDto);

        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Lead"))
                .andExpect(jsonPath("$.price").value(1000L))
                .andExpect(jsonPath("$.pipeline_id").value(1L))
                .andExpect(jsonPath("$.status_id").value(1L));
    }

    @Test
    @Description("Test method POST /leads. Should return 400.")
    void createLead_InvalidStatus_ReturnsBadRequest() throws Exception {
        LeadDto leadDto = TestObjectFactory.createTestLead();
        leadDto.setStatusId(-999L);

        mockMvc.perform(post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(leadDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("status_id"))
                .andExpect(jsonPath("$.errors[0].message").value("Status does not exists"));
    }



    @Test
    @Description("Test method GET /leads/{id}. Should return 404.")
    void getLead_LeadNotFound_ReturnsNotFoundResponse() throws Exception {
        when(leadService.findByIdDto(-1L)).thenThrow(new EntityNotFoundException("Lead not found by ID: -1"));

        mockMvc.perform(get("/leads/{id}", -1L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value("Lead not found by ID: " + -1));
    }

    @Test
    @Description("Test method /leads/{id}. Should return 200.")
    void getLead_LeadFound_ReturnsOkResponse() throws Exception {
        LeadDto leadDto = TestObjectFactory.createTestLead();

        when(leadService.findByIdDto(1L)).thenReturn(leadDto);

        mockMvc.perform(get("/leads/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Test Lead"))
            .andExpect(jsonPath("$.pipeline_id").value(1L))
            .andExpect(jsonPath("$.price").value(1000L))
            .andExpect(jsonPath("$.status_id").value(1L));
    }

    @Test
    void testGetLeads_ReturnsOkResponse() throws Exception{
        LeadDto leadDto = TestObjectFactory.createTestLead();
        List<LeadDto> leads = List.of(leadDto);
        Page<LeadDto> leadPage = new PageImpl<>(leads, PageRequest.of(0, 10), leads.size());

        when(leadService.getLeadsDto(any(Pageable.class))).thenReturn(leadPage);

        mockMvc.perform(get("/leads")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].id").value(1L))
            .andExpect(jsonPath("$.content[0].name").value("Test Lead"))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.size").value(10));
    }


    @Test
    void testGetPipelines_ReturnOkResponse() throws Exception{
        PipelineDto pipelineDto = TestObjectFactory.createTestPipelineDto();

        when(pipelineService.getPipelinesDto()).thenReturn(List.of(pipelineDto));

        mockMvc.perform(get("/leads/pipelines"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Test Pipeline"))
        .andExpect(jsonPath("$[0].statuses").isArray())
        .andExpect(jsonPath("$[0].statuses[0].id").value(1L))
        .andExpect(jsonPath("$[0].statuses[0].name").value("Test Status"));
    }
    @Test
    void testGetPipelineById_PipelineFound_ReturnsOkResponse() throws Exception {
        when(pipelineService.findByIdDto(anyLong())).thenReturn(TestObjectFactory.createTestPipelineDto());

        mockMvc.perform(get("/leads/pipelines/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Test Pipeline"))
        .andExpect(jsonPath("$.statuses").isArray())
        .andExpect(jsonPath("$.statuses[0].id").value(1L))
        .andExpect(jsonPath("$.statuses[0].name").value("Test Status"));
    }

    @Test
    void testGetPipelineById_PipelineNotFound_ReturnsNotFoundResponse() throws Exception {
        when(pipelineService.findByIdDto(anyLong())).thenThrow(new EntityNotFoundException("Pipeline not found by ID: 1"));

        mockMvc.perform(get("/leads/pipelines/{id}", 1L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("Pipeline not found by ID: 1"));
    }

   @Test
    void getStatuses_PipelineFound_ReturnsOkResponse() throws Exception {
        Pipeline pipeline = TestObjectFactory.createTestPipeline();
        when(pipelineService.findById(anyLong())).thenReturn(pipeline);
        when(pipelineService.getStatusesDto(any(Pipeline.class))).thenReturn(TestObjectFactory.createTestStatusesDto());

        mockMvc.perform(get("/leads/pipelines/{pipelineId}/statuses", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Test Status"));
    }

    @Test
    void getStatuses_PipelineNotFound_ReturnsNotFoundResponse() throws Exception {
        when(pipelineService.findById(anyLong())).thenThrow(new EntityNotFoundException("Pipeline not found by ID: 1"));

        mockMvc.perform(get("/leads/pipelines/{pipelineId}/statuses", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Pipeline not found by ID: 1"));
    }

    @Test
    void getStatusById_PipelineFoundStatusFound_ReturnsOkResponse() throws Exception {
        Pipeline pipeline = TestObjectFactory.createTestPipeline();
        StatusDto status = TestObjectFactory.createTestPipelineDto().getStatuses().getFirst();
    
        when(pipelineService.findById(anyLong())).thenReturn(pipeline);
        when(pipelineService.getStatusDto(any(Pipeline.class), anyLong())).thenReturn(status);
    
        mockMvc.perform(get("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Status"));
    }

    @Test
    void getStatusById_PipelineNotFound_ReturnsNotFoundResponse() throws Exception {
        when(pipelineService.findById(anyLong())).thenThrow(new EntityNotFoundException("Pipeline not found by ID: 1"));

        mockMvc.perform(get("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Pipeline not found by ID: 1"));
    }

    @Test
    void getStatusById_PipelineFoundStatusNotFound_ReturnsNotFoundResponse() throws Exception {
        Pipeline pipeline = TestObjectFactory.createTestPipeline();

        when(pipelineService.findById(anyLong())).thenReturn(pipeline);
        when(pipelineService.getStatusDto(any(Pipeline.class), anyLong())).thenThrow(new EntityNotFoundException("Status not found by ID: 1"));
    
        mockMvc.perform(get("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Status not found by ID: 1"));
    }

    @Test
    void testCreatePipeline_NameValid_ReturnsCreatedResponse () throws Exception{

        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("Test Pipeline");

        when(pipelineService.createPipelineDto(anyString())).thenReturn(TestObjectFactory.createTestPipelineDto());

        mockMvc.perform(post("/leads/pipelines")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pipelineDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Test Pipeline"))
        .andExpect(jsonPath("$.statuses").isArray())
        .andExpect(jsonPath("$.statuses[0].id").value(1L))
        .andExpect(jsonPath("$.statuses[0].name").value("Test Status"));
    }

    @Test
    void testCreatePipeline_NameInvalid_ReturnsBadRequestResponse () throws Exception{

        PipelineDto pipelineDto = new PipelineDto();

        mockMvc.perform(post("/leads/pipelines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pipelineDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    void testCreateStatus_NameValidPipelineFound_ReturnsCreatedResponse() throws Exception{
        StatusDto statusDto = new StatusDto();
        statusDto.setName("Test Status");

        when(pipelineService.createStatusDto(anyString(), anyLong())).thenReturn(TestObjectFactory.createStatusDto());
        
        mockMvc.perform(post("/leads/pipelines/{pipelineId}/statuses", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Status"))
                .andExpect(jsonPath("$.pipeline_id").value(1L));
    }

    @Test
    void testCreateStatus_NameValidPipelineNotFound_ReturnsCreatedResponse() throws Exception{
        StatusDto statusDto = new StatusDto();
        statusDto.setName("Test Status");

        when(pipelineService.createStatusDto(anyString(), anyLong())).thenThrow(new EntityNotFoundException("Pipeline not found by ID: 1"));
        
        mockMvc.perform(post("/leads/pipelines/{pipelineId}/statuses", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Pipeline not found by ID: 1"));
    }

    @Test
    void testCreateStatus_NameInvalidPipelineFound_ReturnsCreatedResponse() throws Exception{
        StatusDto statusDto = new StatusDto();
        
        mockMvc.perform(post("/leads/pipelines/{pipelineId}/statuses", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].field").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    void testUpdatePipeline_ValidNamePipelineFound_ReturnsOkResponse() throws Exception{
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("Test Pipeline");

        when(pipelineService.updatePipelineDto(anyString(), anyLong())).thenReturn(TestObjectFactory.createTestPipelineDto());

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pipelineDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Test Pipeline"))
        .andExpect(jsonPath("$.statuses").isArray())
        .andExpect(jsonPath("$.statuses[0].id").value(1L))
        .andExpect(jsonPath("$.statuses[0].name").value("Test Status"));
    }

    @Test
    void testUpdatePipeline_InvalidNamePipelineFound_ReturnsBadRequestResponse() throws Exception{
        PipelineDto pipelineDto = new PipelineDto();

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pipelineDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors[0].field").value("name"))
        .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }

    @Test
    void testUpdatePipeline_ValidNamePipelineNotFound_ReturnsNotFoundResponse() throws Exception{
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setName("Test Pipeline");

        when(pipelineService.updatePipelineDto(anyString(), anyLong())).thenThrow(new EntityNotFoundException("Pipeline not found by ID: 1"));

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pipelineDto))
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
        .andExpect(jsonPath("$.message").value("Pipeline not found by ID: 1"));
    }


    @Test
    void testUpdateStatus_ValidNamePipelineFoundStatusFound_ReturnsOkResponse() throws Exception{
        StatusDto statusDto = new StatusDto();
        statusDto.setName("Test Status");
        when(pipelineService.updateStatusDto(anyString(), anyLong(), anyLong())).thenReturn(TestObjectFactory.createStatusDto());

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Test Status"))
            .andExpect(jsonPath("$.pipeline_id").value(1L));
    }

    @Test
    void testUpdateStatus_ValidNamePipelineFoundStatusNotFound_ReturnsNotFoundResponse() throws Exception{
        StatusDto statusDto = new StatusDto();
        statusDto.setName("Test Status");
        when(pipelineService.updateStatusDto(anyString(), anyLong(), anyLong())).thenThrow(new EntityNotFoundException("Status not found by ID: 1"));

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusDto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value("Status not found by ID: 1"));
    }

    @Test
    void testUpdateStatus_ValidNamePipelineNotFoundStatusFound_ReturnsNotFoundResponse() throws Exception{
        StatusDto statusDto = new StatusDto();
        statusDto.setName("Test Status");
        when(pipelineService.updateStatusDto(anyString(), anyLong(), anyLong())).thenThrow(new EntityNotFoundException("Pipeline not found by ID: 1"));

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusDto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(jsonPath("$.message").value("Pipeline not found by ID: 1"));
    }

    @Test
    void testUpdateStatus_InvalidNamePipelineFoundStatusFound_ReturnsBadRequestResponse() throws Exception{
        StatusDto statusDto = new StatusDto();

        mockMvc.perform(patch("/leads/pipelines/{pipelineId}/statuses/{statusId}", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(jsonPath("$.errors").isArray())
            .andExpect(jsonPath("$.errors[0].field").value("name"))
            .andExpect(jsonPath("$.errors[0].message").value("must not be blank"));
    }
}
