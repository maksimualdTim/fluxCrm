package uz.fluxCrm.fluxCrm.crm.service;

import java.util.List;

import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;

public interface PipelineService {

    public Pipeline createDefault();

    public List<PipelineDto> getPipelinesDto();

    public List<Pipeline> getPipelines();

    public Pipeline findById(Long id);

    public PipelineDto findByIdDto(Long id);

    public PipelineDto createPipelineDto(String name);

    public Pipeline createPipeline(String name);

    public PipelineDto updatePipelineDto(String name, Long pipelineId);

    public Pipeline updatePipeline(String name, Long pipelineId);

    
    
    public List<Status> createDefaultStatuses(Pipeline pipeline);

    public List<StatusDto> getStatusesDto(Pipeline pipeline);

    public StatusDto getStatusDto(Pipeline pipeline, Long id);

    public Status getStatus(Pipeline pipeline, Long id);

    public Status createStatus(String name, Long pipelineId);

    public StatusDto createStatusDto(String name, Long pipelineId);

    public Status updateStatus(String name, Long pipelineId, Long statusId);

    public StatusDto updateStatusDto(String name, Long pipelineId, Long statusId);
}
