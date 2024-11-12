package uz.fluxCrm.fluxCrm.crm.service;

import java.util.List;

import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;

public interface PipelineService {

    public Pipeline createDefault();

    public void addStatus(List<Status> statuses, Long pipelineId);

    public void addStatus(List<Status> statuses, Pipeline pipeline);

    public void addStatus(Status status, Long pipelineId);

    public void addStatus(Status status, Pipeline pipeline);

    public List<PipelineDto> getPipelinesDto();

    public List<Pipeline> getPipelines();

    public Pipeline findById(Long id);

    public PipelineDto findByIdDto(Long id);

    public PipelineDto createPipelineDto(String name);

    public Pipeline creatPipeline(String name);

    public PipelineDto updatePipelineDto(String name, Long pipelineId);

    public Pipeline updatePipeline(String name, Long pipelineId);
}
