package uz.fluxCrm.fluxCrm.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.StatusMapper;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

@Service
@AllArgsConstructor
public class DefaultStatusService implements StatusService{
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;
    private final PipelineService pipelineService;

    public List<Status> createDefault(Pipeline pipeline) {
        List<Status> statuses = new ArrayList<Status>();
        String[] defaultStatuses = new String[]{"Первичный контак", "Отправили договор", "Переговоры", "Принимают решение", "Успешно реализовано", "Закрыто и не реализовано"};
        for (String statusName : defaultStatuses) {
            Status status = new Status();
            status.setName(statusName);
            status.setPipeline(pipeline);
        }

        return statusRepository.saveAll(statuses);
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
        Pipeline pipeline = pipelineService.findById(pipelineId);
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
        Pipeline pipeline = pipelineService.findById(pipelineId);
        Status status = getStatus(pipeline, statusId);
        status.setName(name);
        return statusRepository.save(status);
    }

    @Override
    public StatusDto updateStatusDto(String name, Long pipelineId, Long statusId) {
        return statusMapper.toResponse(updateStatus(name, pipelineId, statusId));
    }
}
