package uz.fluxCrm.fluxCrm.crm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.entity.Lead;
import uz.fluxCrm.fluxCrm.crm.mapper.LeadMapper;
import uz.fluxCrm.fluxCrm.crm.repository.LeadRepository;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

@Service
@AllArgsConstructor
public class DefaultLeadService implements LeadService{

    
    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;
    private final StatusRepository statusRepository;

    @Override
    public Page<Lead> getLeads(Pageable pageable) {
        return leadRepository.findAll(pageable);
    }

    @Override
    public Page<LeadDto> getLeadsDto(Pageable pageable) {
        return getLeads(pageable).map(lead -> leadMapper.toResponse(lead));
    }

    @Override
    public Lead findById(Long id) {
        return leadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lead not found by ID: " + id));
    }

    @Override
    public LeadDto findByIdDto(Long id) {
        return leadMapper.toResponse(findById(id));
    }

    @Override
    public Lead createLead(LeadDto leadDto) {
        Lead lead = leadMapper.toEntity(leadDto, statusRepository);
        return leadRepository.save(lead);
    }

    @Override
    public Lead createLead(Lead lead) {
        return leadRepository.save(lead);
    }

    @Override
    public LeadDto createLeadDto(LeadDto leadDto) {
        Lead lead = createLead(leadDto);
        return leadMapper.toResponse(lead);
    }

    @Override
    public LeadDto createLeadDto(Lead lead) {
        Lead createdlead = createLead(lead);
        return leadMapper.toResponse(createdlead);
    }
}