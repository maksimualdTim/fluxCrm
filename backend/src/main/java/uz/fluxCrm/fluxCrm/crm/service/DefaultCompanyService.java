package uz.fluxCrm.fluxCrm.crm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.entity.Company;
import uz.fluxCrm.fluxCrm.crm.mapper.CompanyMapper;
import uz.fluxCrm.fluxCrm.crm.repository.CompanyRepository;

@Service
@AllArgsConstructor
public class DefaultCompanyService implements CompanyService{
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;

    @Override
    public CompanyDto createDto(CompanyDto companyDto) {
        Company company = companyMapper.toEntity(companyDto);
        company = companyRepository.save(company);
        return companyMapper.toResponse(company);
    }

    @Override
    public CompanyDto findByIdDto(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found by ID: " + id));
        return companyMapper.toResponse(company);
    }

    @Override
    public Page<CompanyDto> getCompaniesDto(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);
        return companies.map(company -> companyMapper.toResponse(company));
    }

    @Override
    public CompanyDto updateDto(CompanyDto companyDto, Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found by ID: " + id));
        company.setName(companyDto.getName() != null ? companyDto.getName() : company.getName());
        company = companyRepository.save(company);
        return companyMapper.toResponse(company);
    }

}
