package uz.fluxCrm.fluxCrm.crm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;

public interface ContactService {
    public Page<ContactDto> getContactsDto(Pageable pageable);
    
    public ContactDto findByIdDto(Long id);

    public ContactDto createDto(ContactDto contactDto);

    public ContactDto updateDto(ContactDto contactDto, Long id);
}
