package uz.fluxCrm.fluxCrm.crm.service;

import java.util.List;

import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;

public interface StatusService {
    public List<Status> createDefault(Pipeline pipeline);

    public List<StatusDto> getStatusesDto(Pipeline pipeline);

    public StatusDto getStatusDto(Pipeline pipeline, Long id);

    public Status getStatus(Pipeline pipeline, Long id);
}
