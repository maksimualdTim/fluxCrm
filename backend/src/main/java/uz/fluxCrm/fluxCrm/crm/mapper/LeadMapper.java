package uz.fluxCrm.fluxCrm.crm.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDtoSimple;
import uz.fluxCrm.fluxCrm.crm.entity.Lead;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {ContactMapper.class, CompanyMapper.class}
)
public interface LeadMapper {
    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.pipeline.id", target = "pipelineId")
    LeadDto toResponse(Lead lead);

    @Mapping(source = "status.id", target = "statusId")
    @Mapping(source = "status.pipeline.id", target = "pipelineId")
    LeadDtoSimple toSimpleResponse(Lead lead);

    @Mapping(source = "statusId", target = "status")
    @Mapping(target = "id", ignore = true)
    Lead toEntity(LeadDto dto, @Context StatusRepository statusRepository);

    @Mapping(source = "statusId", target = "status")
    @Mapping(target = "id", ignore = true)
    Lead toEntity(LeadDtoSimple dto, @Context StatusRepository statusRepository);

    default Status mapStatus(Long statusId, @Context StatusRepository statusRepository) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Status ID"));
    }
}
