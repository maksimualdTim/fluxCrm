package uz.fluxCrm.fluxCrm.crm.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.service.CompanyService;

@RestController
@RequestMapping("/companies")
@AllArgsConstructor
public class CompaniesController {
    private final CompanyService companyService;

    @GetMapping({"", "/"})
    public Page<CompanyDto> getContacts(Pageable pageable) {
        return companyService.getCompaniesDto(pageable);
    }
    
    @GetMapping("/{id}")
    public CompanyDto getContact(@PathVariable Long id) {
        return companyService.findByIdDto(id);
    }
    
    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDto createContact(@Valid @RequestBody CompanyDto companyDto) {
        return companyService.createDto(companyDto);
    }
    
    @PatchMapping("/{id}")
    public CompanyDto updateContact(@Valid @RequestBody CompanyDto companyDto, @PathVariable Long id) {
        return companyService.updateDto(companyDto, id);
    }
}
