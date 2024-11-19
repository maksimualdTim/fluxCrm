package uz.fluxCrm.fluxCrm.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.dto.CompanyDtoSimple;
import uz.fluxCrm.fluxCrm.crm.entity.Company;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    CompanyDto toResponse(Company company);

    CompanyDtoSimple toSimpleResponse(Company company);

    @Mapping(target = "id", ignore = true)
    Company toEntity(CompanyDto companyDto);

    @Mapping(target = "id", ignore = true)
    Company toEntity(CompanyDtoSimple companyDto);
}
