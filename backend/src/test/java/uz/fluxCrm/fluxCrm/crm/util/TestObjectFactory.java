package uz.fluxCrm.fluxCrm.crm.util;

import java.util.List;

import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;

public class TestObjectFactory {
    public static LeadDto createTestLead() {
        LeadDto lead = new LeadDto();
        lead.setId(1L);
        lead.setName("Test Lead");
        lead.setPrice(1000L);
        lead.setPipelineId(1L);
        lead.setStatusId(1L);
        return lead;
    }

    public static Status createStatus() {
        Status status = new Status();
        status.setId(1L);
        status.setName("Test Status");
        return status;
    }

    public static Pipeline createPipeline() {
        Pipeline pipeline = new Pipeline();
        pipeline.setId(1L);
        pipeline.setName("Test Pipeline");
        pipeline.setStatuses(List.of(createStatus()));
        return pipeline;
    }

    public static StatusDto createStatusDto() {
        StatusDto status = new StatusDto();
        status.setId(1L);
        status.setName("Test Status");
        status.setPipelineId(1L);
        return status;
    }

    public static Pipeline createTestPipeline() {
        Pipeline pipeline = new Pipeline();
        Status status = createStatus();
        status.setPipeline(pipeline);
        pipeline.setId(1L);
        pipeline.setName("Test Pipeline");
        pipeline.setStatuses(List.of(status));
        return pipeline;
    }

    public static PipelineDto createTestPipelineDto() {
        PipelineDto pipelineDto = new PipelineDto();
        pipelineDto.setId(1L);
        pipelineDto.setName("Test Pipeline");
        pipelineDto.setStatuses(createTestStatusesDto());
        return pipelineDto;
    }

    public static List<StatusDto> createTestStatusesDto() {
        StatusDto status = new StatusDto();
        status.setId(1L);
        status.setName("Test Status");
        status.setPipelineId(1L);
        return List.of(status);
    }
}
