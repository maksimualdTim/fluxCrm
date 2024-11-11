package uz.fluxCrm.fluxCrm.crm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.repository.PipelineRepository;

public class PipelineServiceTest {
    @Mock
    private PipelineRepository pipelineRepository;

    @InjectMocks
    private DefaultPipelineService pipelineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Description("Test createDefault method")
    void testCreateDeafault() {
        Pipeline expectedPipeline = new Pipeline();
        expectedPipeline.setName("Воронка");

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(expectedPipeline);

        Pipeline result = pipelineService.createDefault();

        assertEquals("Воронка", result.getName());
        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
    }


    @Test
    @Description("Test addStatus List statuses method with Pipeline ID")
    void testAddStatus_ListStatusesWithPipelineId() {
        Long pipelineId = 1L;
        Status status1 = new Status();
        status1.setName("New");
        Status status2 = new Status();
        status2.setName("In Progress");

        List<Status> statuses = List.of(status1, status2);

        Pipeline pipeline = new Pipeline();
        pipeline.setId(pipelineId);
        pipeline.setStatuses(new ArrayList<>());

        when(pipelineRepository.findById(pipelineId)).thenReturn(Optional.of(pipeline));
        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);

        pipelineService.addStatus(statuses, pipelineId);

        verify(pipelineRepository).save(pipeline);
        assertEquals(2, pipeline.getStatuses().size());
    }

    @Test
    @Description("Test addStatus List statuses method with Pipeline Object")
    void testAddStatus_ListStatusesWithPipelineObject() {
        Pipeline pipeline = new Pipeline();
        Status status1 = new Status();
        status1.setName("New");
        Status status2 = new Status();
        status2.setName("In Progress");

        List<Status> statuses = List.of(status1, status2);
        pipeline.setStatuses(new ArrayList<>());

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);

        pipelineService.addStatus(statuses, pipeline);

        verify(pipelineRepository).save(pipeline);
        assertEquals(2, pipeline.getStatuses().size());
    }

    @Test
    @Description("Test addStatus single status method with Pipeline ID")
    void testAddStatus_SingleStatusWithPipelineId() {
        Long pipelineId = 1L;
        Status status = new Status();
        status.setName("New");

        Pipeline pipeline = new Pipeline();
        pipeline.setId(pipelineId);
        pipeline.setStatuses(new ArrayList<>());

        when(pipelineRepository.findById(pipelineId)).thenReturn(Optional.of(pipeline));
        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);

        pipelineService.addStatus(status, pipelineId);

        verify(pipelineRepository).save(pipeline);
        assertEquals(1, pipeline.getStatuses().size());
        assertEquals("New", pipeline.getStatuses().get(0).getName());
    }

    @Test
    @Description("Test addStatus single status method with Pipeline Object")
    void testAddStatus_SingleStatusWithPipelineObject() {
        Pipeline pipeline = new Pipeline();
        Status status = new Status();
        status.setName("New");
        pipeline.setStatuses(new ArrayList<>());

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);

        pipelineService.addStatus(status, pipeline);

        verify(pipelineRepository).save(pipeline);
        assertEquals(1, pipeline.getStatuses().size());
        assertEquals("New", pipeline.getStatuses().get(0).getName());
    }
}
