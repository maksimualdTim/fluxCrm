package uz.fluxCrm.fluxCrm.crm.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.PipelineMapper;
import uz.fluxCrm.fluxCrm.crm.mapper.StatusMapper;
import uz.fluxCrm.fluxCrm.crm.repository.PipelineRepository;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;
import uz.fluxCrm.fluxCrm.crm.service.DefaultPipelineService;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@ExtendWith(MockitoExtension.class)
public class DefaultPipelineServiceTest {

    @Mock
    private PipelineRepository pipelineRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private PipelineMapper pipelineMapper;

    @Mock
    private StatusMapper statusMapper;

    @InjectMocks
    private DefaultPipelineService pipelineService;

    @Test
    void testCreatePipeline_ReturnsPipeline() {
        Pipeline expectedPipeline = TestObjectFactory.createPipeline();

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(expectedPipeline);

        Pipeline result = pipelineService.createPipeline("Test Pipeline");

        assertEquals("Test Pipeline", result.getName());
        assertEquals(1L, result.getId());
        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
    }

    @Test
    void testCreatePipelineDto_ReturnsPipelineDto() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        PipelineDto pipelineDto = TestObjectFactory.createTestPipelineDto();

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);
        when(pipelineMapper.toResponse(any(Pipeline.class))).thenReturn(pipelineDto);

        PipelineDto result = pipelineService.createPipelineDto("Test Pipeline");

        assertEquals("Test Pipeline", result.getName());
        assertEquals(1L, result.getId());
        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
    }

    @Test
    void testCreateStatus_PipelineFound_ReturnsStatus() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);
        Status status = TestObjectFactory.createStatus();
        status.setPipeline(pipeline);

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        when(statusRepository.save(any(Status.class))).thenReturn(status);

        Status result = pipelineService.createStatus("Test Status", 1L);

        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPipeline().getId());

        verify(statusRepository, times(1)).save(any(Status.class));
    }

    @Test
    void testCreateStatus_PipelineNotFound_ThrowsException() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.empty();
        Status status = TestObjectFactory.createStatus();
        status.setPipeline(pipeline);

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.createStatus("Test Status", -1L));
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(0)).save(any(Status.class));
    }

    @Test
    void testCreateStatusDto_PipelineFound_ReturnsStatus() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);
        Status status = TestObjectFactory.createStatus();
        status.setPipeline(pipeline);
        StatusDto statusDto = TestObjectFactory.createStatusDto();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        when(statusRepository.save(any(Status.class))).thenReturn(status);
        when(statusMapper.toResponse(any(Status.class))).thenReturn(statusDto);

        StatusDto result = pipelineService.createStatusDto("Test Status", 1L);

        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getPipelineId());

        verify(statusRepository, times(1)).save(any(Status.class));
    }

    @Test
    void testCreateStatusDto_PipelineNotFound_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.createStatus("Test Status", -1L));
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(0)).save(any(Status.class));
    }

    @Test
    void testFindById_ReturnsPipeline() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);
        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        Pipeline result = pipelineService.findById(1L);

        assertEquals("Test Pipeline", result.getName());
        assertEquals(1L, result.getId());

        verify(pipelineRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindById_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();
        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.findById(1L));

        verify(pipelineRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByIdDto_ReturnsPipelineDto() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);
        PipelineDto pipelineDto = TestObjectFactory.createTestPipelineDto();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        when(pipelineMapper.toResponse(any(Pipeline.class))).thenReturn(pipelineDto);

        PipelineDto result = pipelineService.findByIdDto(1L);

        assertEquals("Test Pipeline", result.getName());
        assertEquals(1L, result.getId());

        verify(pipelineRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByIdDto_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();
        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.findByIdDto(1L));

        verify(pipelineRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetPipelines_ReturnsListPipeline() {
        Pipeline pipeline = TestObjectFactory.createPipeline();

        when(pipelineRepository.findAll()).thenReturn(List.of(pipeline));

        List<Pipeline> pipelines = pipelineService.getPipelines();

        assertEquals(1, pipelines.size());
        assertEquals("Test Pipeline", pipelines.getFirst().getName());
        assertEquals(1L, pipelines.getFirst().getId());
    }

    @Test
    void testGetPipelinesDto_ReturnsListPipelineDto() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        PipelineDto pipelineDto = TestObjectFactory.createTestPipelineDto();

        when(pipelineRepository.findAll()).thenReturn(List.of(pipeline));
        when(pipelineMapper.toResponse(anyList())).thenReturn(List.of(pipelineDto));

        List<PipelineDto> pipelines = pipelineService.getPipelinesDto();

        assertEquals(1, pipelines.size());
        assertEquals("Test Pipeline", pipelines.getFirst().getName());
        assertEquals(1L, pipelines.getFirst().getId());
    }

    @Test
    void testGetStatus_StatusFound_ReturnsStatus() {
        Pipeline pipeline = TestObjectFactory.createPipeline();

        Status result = pipelineService.getStatus(pipeline, 1L);

        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetStatus_StatusNotFound_ThrowsException() {
        Pipeline pipeline = TestObjectFactory.createPipeline();

        assertThrows(EntityNotFoundException.class, () -> pipelineService.getStatus(pipeline, -1L));
    }

    @Test
    void testGetStatusDto_StatusFound_ReturnsStatusDto() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        StatusDto statusDto = TestObjectFactory.createStatusDto();

        when(statusMapper.toResponse(any(Status.class))).thenReturn(statusDto);

        StatusDto result = pipelineService.getStatusDto(pipeline, 1L);

        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetStatusDto_StatusNotFound_ThrowsException() {
        Pipeline pipeline = TestObjectFactory.createPipeline();

        assertThrows(EntityNotFoundException.class, () -> pipelineService.getStatusDto(pipeline, -1L));
    }

    @Test
    void testGetStatusesDto_ReturnsListStatusDto() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        StatusDto statusDto = TestObjectFactory.createStatusDto();

        when(statusMapper.toResponse(anyList())).thenReturn(List.of(statusDto));

        List<StatusDto> pipelines = pipelineService.getStatusesDto(pipeline);

        assertEquals(1, pipelines.size());
        assertEquals("Test Status", pipelines.getFirst().getName());
        assertEquals(1L, pipelines.getFirst().getId());
    }

    @Test
    void testUpdatePipeline_PipelineFound_ReturnsPipeline() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);
        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        Pipeline result = pipelineService.updatePipeline("Test Pipeline", 1L);

        assertEquals("Test Pipeline", result.getName());
        assertEquals(1L, result.getId());

        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
    }

    @Test
    void testUpdatePipeline_PipelineNotFound_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        assertThrows(EntityNotFoundException.class, () -> pipelineService.updatePipeline("Test Pipeline", -1L));

        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(pipelineRepository, times(0)).save(any(Pipeline.class));
    }

    @Test
    void testUpdatePipelineDto_PipelineFound_ReturnsPipeline() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);

        when(pipelineRepository.save(any(Pipeline.class))).thenReturn(pipeline);
        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        when(pipelineMapper.toResponse(any(Pipeline.class))).thenReturn(TestObjectFactory.createTestPipelineDto());

        PipelineDto result = pipelineService.updatePipelineDto("Test Pipeline", 1L);

        assertEquals("Test Pipeline", result.getName());
        assertEquals(1L, result.getId());

        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(pipelineRepository, times(1)).save(any(Pipeline.class));
    }

    @Test
    void testUpdatePipelineDto_PipelineNotFound_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        assertThrows(EntityNotFoundException.class, () -> pipelineService.updatePipelineDto("Test Pipeline", -1L));

        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(pipelineRepository, times(0)).save(any(Pipeline.class));
    }

    @Test
    void testUpdateStatus_PipelineFoundStatusFound_ReturnsStatus() {
        Status status = TestObjectFactory.createStatus();
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        when(statusRepository.save(any(Status.class))).thenReturn(status);

        Status result = pipelineService.updateStatus("Test Status", 1L, 1L);

        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getId());
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(1)).save(any(Status.class));
        verify(statusRepository, times(0)).findById(anyLong());
    }

    @Test
    void testUpdateStatus_PipelineNotFoundStatusFound_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.updateStatus("Test Status", -1L, 1L));
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(0)).save(any(Status.class));
        verify(statusRepository, times(0)).findById(anyLong());
    }

    @Test
    void testUpdateStatus_PipelineFoundStatusNotFound_ThrowsException() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.updateStatus("Test Status", 1L, -1L));
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(0)).save(any(Status.class));
        verify(statusRepository, times(0)).findById(anyLong());
    }

    @Test
    void testUpdateStatusDto_PipelineFound_StatusFound() {
        Status status = TestObjectFactory.createStatus();
        StatusDto statusDto = TestObjectFactory.createStatusDto();
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);
        when(statusRepository.save(any(Status.class))).thenReturn(status);
        when(statusMapper.toResponse(any(Status.class))).thenReturn(statusDto);

        StatusDto result = pipelineService.updateStatusDto("Test Status", 1L, 1L);

        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getId());
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(1)).save(any(Status.class));
        verify(statusRepository, times(0)).findById(anyLong());
    }

    @Test
    void testUpdateStatusDto_PipelineNotFoundStatusFound_ThrowsException() {
        Optional<Pipeline> pipelineOptional = Optional.empty();

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.updateStatusDto("Test Status", -1L, 1L));
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(0)).save(any(Status.class));
        verify(statusRepository, times(0)).findById(anyLong());
    }

    @Test
    void testUpdateStatusDto_PipelineFoundStatusNotFound_ThrowsException() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        Optional<Pipeline> pipelineOptional = Optional.of(pipeline);

        when(pipelineRepository.findById(anyLong())).thenReturn(pipelineOptional);

        assertThrows(EntityNotFoundException.class, () -> pipelineService.updateStatusDto("Test Status", 1L, -1L));
        verify(pipelineRepository, times(1)).findById(anyLong());
        verify(statusRepository, times(0)).save(any(Status.class));
        verify(statusRepository, times(0)).findById(anyLong());
    }
}
