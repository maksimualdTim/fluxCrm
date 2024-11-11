package uz.fluxCrm.fluxCrm.crm.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Status;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusMapper {
    
    @Mapping(source = "pipeline.id", target = "pipelineId")
    StatusDto toResponse(Status status);

    List<StatusDto> toResponse(List<Status> statuses);
}
