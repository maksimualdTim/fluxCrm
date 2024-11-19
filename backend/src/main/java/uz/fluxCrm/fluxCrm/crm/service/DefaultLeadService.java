package uz.fluxCrm.fluxCrm.crm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.entity.Company;
import uz.fluxCrm.fluxCrm.crm.entity.Contact;
import uz.fluxCrm.fluxCrm.crm.entity.Lead;
import uz.fluxCrm.fluxCrm.crm.mapper.LeadMapper;
import uz.fluxCrm.fluxCrm.crm.repository.CompanyRepository;
import uz.fluxCrm.fluxCrm.crm.repository.ContactRepository;
import uz.fluxCrm.fluxCrm.crm.repository.LeadRepository;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

@Service
@AllArgsConstructor
public class DefaultLeadService implements LeadService{

    
    private final LeadRepository leadRepository;
    private final LeadMapper leadMapper;
    private final StatusRepository statusRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;

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
    @Transactional
    public Lead createLead(LeadDto leadDto) {
        Lead lead = leadMapper.toEntity(leadDto, statusRepository);
        Company company = lead.getCompany();
        if(company != null) {
            company = companyRepository.save(company);
            lead.setCompany(company);
        }
        
        List<Contact> contacts = lead.getContacts();
        if(!contacts.isEmpty()) {
            contacts = contactRepository.saveAll(contacts);
            lead.setContacts(contacts);
        }

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

    @Override
    public void delete(Long id) {
        Lead lead = findById(id);
        leadRepository.delete(lead);
    }

    @Override
    public LeadDto updateDto(LeadDto leadDto, Long id) {
        Lead lead = leadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lead not found by ID: " + id));
        lead.setName(leadDto.getName() != null ? leadDto.getName() : lead.getName());
        lead.setPrice(leadDto.getPrice() != null ? leadDto.getPrice() : lead.getPrice());
        lead.setCreatedAt(leadDto.getCreatedAt() != null ? leadDto.getCreatedAt() : lead.getCreatedAt());
        lead.setUpdatedAt(leadDto.getUpdatedAt() != null ? leadDto.getUpdatedAt() : lead.getUpdatedAt());

        lead = leadRepository.save(lead);
        return leadMapper.toResponse(lead);
    }
}