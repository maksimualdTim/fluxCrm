package uz.fluxCrm.fluxCrm.crm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;

public interface CompanyService {

    public Page<CompanyDto> getCompaniesDto(Pageable pageable);
    
    public CompanyDto findByIdDto(Long id);

    public CompanyDto createDto(CompanyDto companyDto);

    public CompanyDto updateDto(CompanyDto companyDto, Long id);
}
