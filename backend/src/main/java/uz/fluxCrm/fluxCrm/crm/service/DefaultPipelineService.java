package uz.fluxCrm.fluxCrm.crm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.PipelineMapper;
import uz.fluxCrm.fluxCrm.crm.repository.PipelineRepository;

@Service
@AllArgsConstructor
public class DefaultPipelineService implements PipelineService{
    private final PipelineRepository pipelineRepository;
    private final PipelineMapper pipelineMapper;

    public Pipeline createDefault() {
        Pipeline pipeline = new Pipeline();
        pipeline.setName("Воронка");
        return pipelineRepository.save(pipeline);
    }


    public void addStatus(List<Status> statuses, Long pipelineId) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new EntityNotFoundException("Pipeline not found by ID: " + pipelineId));
        pipeline.getStatuses().addAll(statuses);
        pipelineRepository.save(pipeline);
    }


    public void addStatus(List<Status> statuses, Pipeline pipeline) {
        pipeline.getStatuses().addAll(statuses);
        pipelineRepository.save(pipeline);
    }


    public void addStatus(Status status, Long pipelineId) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new EntityNotFoundException("Pipeline not found by ID: " + pipelineId));
        pipeline.getStatuses().add(status);
        pipelineRepository.save(pipeline);
    }


    public void addStatus(Status status, Pipeline pipeline) {
        pipeline.getStatuses().add(status);
        pipelineRepository.save(pipeline);
    }


    @Override
    public Pipeline findById(Long id) {
        return pipelineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pipeline not found by ID: " + id));
    }


    @Override
    public PipelineDto findByIdDto(Long id) {
        return pipelineMapper.toResponse(findById(id));
    }


    @Override
    public List<Pipeline> getPipelines() {
        return pipelineRepository.findAll();
    }


    @Override
    public List<PipelineDto> getPipelinesDto() {
        List<Pipeline> pipelines = pipelineRepository.findAll();
        return pipelineMapper.toResponse(pipelines);
    }

    @Override
    public PipelineDto createPipelineDto(String name) {
        return pipelineMapper.toResponse(creatPipeline(name));
    }

    @Override
    public Pipeline creatPipeline(String name) {
        Pipeline pipeline = new Pipeline();
        pipeline.setName(name);
        return pipelineRepository.save(pipeline);
    }

    @Override
    public Pipeline updatePipeline(String name, Long pipelineId) {
        Pipeline pipeline = findById(pipelineId);
        pipeline.setName(name);
        return pipelineRepository.save(pipeline);
    }

    @Override
    public PipelineDto updatePipelineDto(String name, Long pipelineId) {
        return pipelineMapper.toResponse(updatePipeline(name, pipelineId));
    }
}
