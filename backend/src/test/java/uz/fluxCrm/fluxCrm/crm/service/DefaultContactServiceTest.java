package uz.fluxCrm.fluxCrm.crm.service;

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
import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;
import uz.fluxCrm.fluxCrm.crm.entity.Contact;
import uz.fluxCrm.fluxCrm.crm.mapper.ContactMapper;
import uz.fluxCrm.fluxCrm.crm.repository.ContactRepository;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@ExtendWith(MockitoExtension.class)
public class DefaultContactServiceTest {

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private DefaultContactService contactService;

    @Test
    void testCreateDto_ReturnsContactDto() {
        ContactDto contactDto = TestObjectFactory.createContactDto();
        Contact contact = TestObjectFactory.createContact();

        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(contactMapper.toEntity(contactDto)).thenReturn(contact);
        when(contactMapper.toResponse(contact)).thenReturn(contactDto);

        ContactDto result = contactService.createDto(contactDto);

        assertEquals(contactDto.getName(), result.getName());

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void testFindByIdDto_ContactFound_ReturnsContactDto() {
        ContactDto contactDto = TestObjectFactory.createContactDto();
        Contact contact = TestObjectFactory.createContact();
        Optional<Contact> contactOptional = Optional.of(contact);

        when(contactRepository.findById(anyLong())).thenReturn(contactOptional);
        when(contactMapper.toResponse(any(Contact.class))).thenReturn(contactDto);

        ContactDto result = contactService.findByIdDto(1L);

        assertEquals("Test Contact", result.getName());
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2024, 1, 3, 10, 32), result.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 1, 3, 10, 32), result.getUpdatedAt());

        verify(contactRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByIdDto_ContactNotFound_ThrowsException() {
        when(contactRepository.findById(anyLong())).thenThrow(new EntityNotFoundException("Contact not found by ID: 1"));

        assertThrows(EntityNotFoundException.class, () -> contactService.findByIdDto(-1L));

        verify(contactRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetCompaniesDto_ReturnsContactPage() {
        Pageable pageable = PageRequest.of(0, 10);
        ContactDto contactDto = TestObjectFactory.createContactDto();
        Contact contact = TestObjectFactory.createContact();
        List<Contact> companies = List.of(contact);
        Page<Contact> companiesPage = new PageImpl<>(companies, pageable, companies.size());

        when(contactRepository.findAll(pageable)).thenReturn(companiesPage);
        when(contactMapper.toResponse(any(Contact.class))).thenReturn(contactDto);
        when(contactMapper.toResponse(contact)).thenReturn(contactDto);

        Page<ContactDto> result = contactService.getContactsDto(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Test Contact", result.getContent().getFirst().getName());

        verify(contactRepository, times(1)).findAll(pageable);
        verify(contactMapper, times(1)).toResponse(any(Contact.class));
    }

    @Test
    void testUpdateDto_ContactFound_ReturnsContact() {
        ContactDto contactDto = TestObjectFactory.createContactDto();
        Contact contact = TestObjectFactory.createContact();
        Optional<Contact> contactOptional = Optional.of(contact);

        when(contactRepository.findById(anyLong())).thenReturn(contactOptional);
        when(contactRepository.save(any(Contact.class))).thenReturn(contact);
        when(contactMapper.toResponse(contact)).thenReturn(contactDto);
        
        ContactDto result = contactService.updateDto(contactDto, 1L);

        assertEquals("Test Contact", result.getName());
        assertEquals(1L, result.getId());

        verify(contactRepository, times(1)).findById(anyLong());
        verify(contactMapper, times(1)).toResponse(any(Contact.class));
    }

    @Test
    void testUpdateDto_ContactNotFound_ThrowsException() {
        ContactDto contactDto = TestObjectFactory.createContactDto();

        when(contactRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> contactService.updateDto(contactDto, -1L));

        verify(contactRepository, times(1)).findById(anyLong());
        verify(contactRepository, times(0)).save(any(Contact.class));
    }
}
