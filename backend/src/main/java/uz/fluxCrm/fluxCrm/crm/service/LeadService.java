package uz.fluxCrm.fluxCrm.crm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.entity.Lead;

public interface LeadService {

    public Page<LeadDto> getLeadsDto(Pageable pageable);

    public Page<Lead> getLeads(Pageable pageable);

    public Lead findById(Long id);

    public LeadDto findByIdDto(Long id);

    public Lead createLead(LeadDto leadDto);
    
    public Lead createLead(Lead lead);

    public LeadDto createLeadDto(LeadDto leadDto);

    public LeadDto createLeadDto(Lead leadDto);
}
