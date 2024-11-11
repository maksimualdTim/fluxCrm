package uz.fluxCrm.fluxCrm.crm.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = StatusMapper.class)
public interface PipelineMapper {
    
    @Mapping(source = "statuses", target = "statuses")
    PipelineDto toResponse(Pipeline pipeline);

    List<PipelineDto> toResponse(List<Pipeline> pipelines);
}
