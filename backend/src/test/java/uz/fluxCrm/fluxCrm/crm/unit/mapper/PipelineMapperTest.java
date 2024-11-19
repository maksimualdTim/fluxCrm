package uz.fluxCrm.fluxCrm.crm.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.PipelineMapperImpl;
import uz.fluxCrm.fluxCrm.crm.mapper.StatusMapper;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@ExtendWith(MockitoExtension.class)
public class PipelineMapperTest {

    @Mock
    private StatusMapper statusMapper;

    @InjectMocks
    private PipelineMapperImpl pipelineMapper;

    @Test
    void testToResponse_ReturnsPipelineDto() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        pipeline.setId(1L);
        Status status = TestObjectFactory.createStatus();
        status.setPipeline(pipeline);
        status.setId(1L);
        pipeline.setStatuses(List.of(status));

        StatusDto statusDto = TestObjectFactory.createStatusDto();

        when(statusMapper.toResponse(anyList())).thenReturn(List.of(statusDto));


        PipelineDto pipelineDto = pipelineMapper.toResponse(pipeline);

        assertNotNull(pipelineDto);
        assertEquals("Test Pipeline", pipelineDto.getName());
        List<StatusDto> statuses = pipelineDto.getStatuses();
        assertEquals(1L, pipelineDto.getId());
        assertNotNull(pipelineDto.getStatuses());
        assertEquals(1L, statuses.getFirst().getId());
        assertEquals("Test Status", statuses.getFirst().getName());
        assertEquals(1L, statuses.getFirst().getPipelineId());
    }
}
