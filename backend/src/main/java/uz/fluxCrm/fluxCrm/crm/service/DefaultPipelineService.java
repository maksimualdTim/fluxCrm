package uz.fluxCrm.fluxCrm.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.config.TenantContext;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Lead;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.PipelineMapper;
import uz.fluxCrm.fluxCrm.crm.mapper.StatusMapper;
import uz.fluxCrm.fluxCrm.crm.repository.LeadRepository;
import uz.fluxCrm.fluxCrm.crm.repository.PipelineRepository;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

@Service
@AllArgsConstructor
public class DefaultPipelineService implements PipelineService{
    private final PipelineRepository pipelineRepository;
    private final LeadRepository leadRepository;

    @Override
    public void deletePipeline(Long id) {
        Pipeline pipeline = findById(id);
        pipelineRepository.delete(pipeline);
    }


    @Override
    public void deleteStatus(Long pipelineId, Long statusId) {
        Pipeline pipeline = findById(pipelineId);
        Status status = getStatus(pipeline, statusId);

        if(status.isMain()) {
            throw new IllegalArgumentException("Status " + status.getName() + " with ID: " + statusId + " is main");
        }

        Status mainStatus = pipeline.getStatuses()
        .stream()
        .filter(Status::isMain)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Main status not found in pipeline"));

        List<Lead> leadsToReassign = status.getLeads();

        for (Lead lead : leadsToReassign) {
            lead.setStatus(mainStatus);
        }

        leadRepository.saveAll(leadsToReassign);

        pipeline.getStatuses().remove(status);
        statusRepository.delete(status);
    }

    private final PipelineMapper pipelineMapper;

    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

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
        List<Pipeline> pipelines = getPipelines();
        return pipelineMapper.toResponse(pipelines);
    }

    @Override
    public PipelineDto createPipelineDto(String name) {
        return pipelineMapper.toResponse(createPipeline(name, false));
    }

    @Override
    @Transactional
    public Pipeline createPipeline(String name, boolean isMain) {
        Pipeline pipeline = new Pipeline();
        pipeline.setName(name);
        pipeline.setMain(isMain);

        List<Status> statuses = new ArrayList<Status>();

        String[] defaultStatuses = new String[]{
            "Первичный контак", 
            "Отправили договор", 
            "Переговоры", 
            "Принимают решение", 
            "Успешно реализовано", 
            "Закрыто и не реализовано"
        };
    
        int i = 0;
        for (String statusName : defaultStatuses) {
            Status status = new Status();
            status.setName(statusName);
            if(i == 0) {
                status.setMain(true);
            }
            status.setPipeline(pipeline);
            status.setAccountId(TenantContext.getCurrentAccountId());
            statuses.add(status);
            i++;
        }
        pipeline.setStatuses(statuses);
        
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

    @Override
    public List<StatusDto> getStatusesDto(Pipeline pipeline) {
        return statusMapper.toResponse(pipeline.getStatuses());
    }

    @Override
    public Status getStatus(Pipeline pipeline, Long id) {
        List<Status> statuses = pipeline.getStatuses();

        Optional<Status> statusOptional = statuses.stream()
        .filter(status -> status.getId().equals(id))
        .findFirst();


        return statusOptional.orElseThrow(() -> new EntityNotFoundException("Status not found by ID: " + id));
    }

    @Override
    public StatusDto getStatusDto(Pipeline pipeline, Long id) {
        return statusMapper.toResponse(getStatus(pipeline, id));
    }

    @Override
    public Status createStatus(String name, Long pipelineId) {
        Pipeline pipeline = findById(pipelineId);
        Status status = new Status();
        status.setName(name);
        status.setPipeline(pipeline);
        return statusRepository.save(status);
    }

    @Override
    public StatusDto createStatusDto(String name, Long pipelineId) {
        return statusMapper.toResponse(createStatus(name, pipelineId));
    }

    @Override
    public Status updateStatus(String name, Long pipelineId, Long statusId) {
        Pipeline pipeline = findById(pipelineId);
        Status status = getStatus(pipeline, statusId);
        status.setName(name);
        return statusRepository.save(status);
    }

    @Override
    public StatusDto updateStatusDto(String name, Long pipelineId, Long statusId) {
        return statusMapper.toResponse(updateStatus(name, pipelineId, statusId));
    }
}
