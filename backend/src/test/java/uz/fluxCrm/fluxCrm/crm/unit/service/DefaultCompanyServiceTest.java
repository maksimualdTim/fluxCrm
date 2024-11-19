package uz.fluxCrm.fluxCrm.crm.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.entity.Company;
import uz.fluxCrm.fluxCrm.crm.mapper.CompanyMapper;
import uz.fluxCrm.fluxCrm.crm.repository.CompanyRepository;
import uz.fluxCrm.fluxCrm.crm.service.DefaultCompanyService;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@ExtendWith(MockitoExtension.class)
public class DefaultCompanyServiceTest {

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private DefaultCompanyService companyService;

    @Test
    void testCreateDto_ReturnsCompanyDto() {
        CompanyDto companyDto = TestObjectFactory.createCompanyDto();
        Company company = TestObjectFactory.createCompany();

        when(companyRepository.save(any(Company.class))).thenReturn(company);
        when(companyMapper.toEntity(companyDto)).thenReturn(company);
        when(companyMapper.toResponse(any(Company.class))).thenReturn(companyDto);

        CompanyDto result = companyService.createDto(companyDto);

        assertEquals(companyDto.getName(), result.getName());

        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void testFindByIdDto_CompanyFound_ReturnsCompanyDto() {
        CompanyDto companyDto = TestObjectFactory.createCompanyDto();
        Company company = TestObjectFactory.createCompany();
        Optional<Company> companyOptional = Optional.of(company);

        when(companyRepository.findById(anyLong())).thenReturn(companyOptional);
        when(companyMapper.toResponse(any(Company.class))).thenReturn(companyDto);

        CompanyDto result = companyService.findByIdDto(1L);

        assertEquals("Test Company", result.getName());
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2024, 1, 3, 10, 32), result.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 1, 3, 10, 32), result.getUpdatedAt());

        verify(companyRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByIdDto_CompanyNotFound_ThrowsException() {
        when(companyRepository.findById(anyLong())).thenThrow(new EntityNotFoundException("Company not found by ID: 1"));

        assertThrows(EntityNotFoundException.class, () -> companyService.findByIdDto(-1L));

        verify(companyRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetCompaniesDto_ReturnsCompanyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        CompanyDto companyDto = TestObjectFactory.createCompanyDto();
        Company company = TestObjectFactory.createCompany();
        List<Company> companies = List.of(company);
        Page<Company> companiesPage = new PageImpl<>(companies, pageable, companies.size());

        when(companyRepository.findAll(pageable)).thenReturn(companiesPage);
        when(companyMapper.toResponse(any(Company.class))).thenReturn(companyDto);

        Page<CompanyDto> result = companyService.getCompaniesDto(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Company", result.getContent().getFirst().getName());

        verify(companyRepository, times(1)).findAll(pageable);
        verify(companyMapper, times(1)).toResponse(any(Company.class));
    }

    @Test
    void testUpdateDto_CompanyFound_ReturnsCompany() {
        CompanyDto companyDto = TestObjectFactory.createCompanyDto();
        Company company = TestObjectFactory.createCompany();
        Optional<Company> companyOptional = Optional.of(company);

        when(companyRepository.findById(anyLong())).thenReturn(companyOptional);
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        when(companyMapper.toResponse(any(Company.class))).thenReturn(companyDto);
        
        CompanyDto result = companyService.updateDto(companyDto, 1L);

        assertEquals("Test Company", result.getName());
        assertEquals(1L, result.getId());

        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyMapper, times(1)).toResponse(any(Company.class));
    }

    @Test
    void testUpdateDto_CompanyNotFound_ThrowsException() {
        CompanyDto companyDto = TestObjectFactory.createCompanyDto();

        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> companyService.updateDto(companyDto, -1L));

        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyRepository, times(0)).save(any(Company.class));
    }
}
