package uz.fluxCrm.fluxCrm.crm.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.StatusMapper;
import uz.fluxCrm.fluxCrm.crm.mapper.StatusMapperImpl;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@ExtendWith(MockitoExtension.class)
public class StatusMapperTest {

    private StatusMapper statusMapper;

    @Test
    void testToResponse_ReturnsStatusDto() {
        statusMapper = new StatusMapperImpl();
        Pipeline pipeline = TestObjectFactory.createPipeline();
        pipeline.setId(1L);
        Status status = TestObjectFactory.createStatus();
        status.setPipeline(pipeline);
        status.setId(1L);
        pipeline.setStatuses(List.of(status));

        StatusDto result = statusMapper.toResponse(pipeline.getStatuses().getFirst());

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Status", result.getName());
        assertEquals(1L, result.getPipelineId());
    }
}
