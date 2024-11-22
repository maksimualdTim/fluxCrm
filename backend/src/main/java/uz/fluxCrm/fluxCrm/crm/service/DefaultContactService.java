package uz.fluxCrm.fluxCrm.crm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;
import uz.fluxCrm.fluxCrm.crm.entity.Contact;
import uz.fluxCrm.fluxCrm.crm.mapper.ContactMapper;
import uz.fluxCrm.fluxCrm.crm.repository.ContactRepository;

@AllArgsConstructor
@Service
public class DefaultContactService implements ContactService{
    private final ContactMapper contactMapper;
    private final ContactRepository contactRepository;

        @Override
    public ContactDto createDto(ContactDto contactDto) {
        Contact contact = contactMapper.toEntity(contactDto);
        contact = contactRepository.save(contact);
        return contactMapper.toResponse(contact);
    }

    @Override
    public ContactDto findByIdDto(Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found by ID: " + id));
        return contactMapper.toResponse(contact);
    }

    @Override
    public Page<ContactDto> getContactsDto(Pageable pageable) {
        Page<Contact> contacts = contactRepository.findAll(pageable);
        return contacts.map(contact -> contactMapper.toResponse(contact));
    }

    @Override
    public ContactDto updateDto(ContactDto contactDto, Long id) {
        Contact contact = contactRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Company not found by ID: " + id));
        contact.setName(contactDto.getName() != null ? contactDto.getName() : contact.getName());
        contact = contactRepository.save(contact);
        return contactMapper.toResponse(contact);
    }
}
