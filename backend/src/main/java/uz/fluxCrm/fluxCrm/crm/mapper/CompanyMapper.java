package uz.fluxCrm.fluxCrm.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.entity.Company;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompanyMapper {

    CompanyDto toResponse(Company company);

    Company toEntity(CompanyDto companyDto);
}