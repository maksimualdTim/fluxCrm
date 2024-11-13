package uz.fluxCrm.fluxCrm.crm.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;
import uz.fluxCrm.fluxCrm.crm.service.ContactService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/contacts")
@AllArgsConstructor
public class ContactsController {

    private final ContactService contactService;

    @GetMapping({"", "/"})
    public Page<ContactDto> getContacts(Pageable pageable) {
        return contactService.getContactsDto(pageable);
    }
    
    @GetMapping("/{id}")
    public ContactDto getContact(@PathVariable Long id) {
        return contactService.findByIdDto(id);
    }
    
    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDto createContact(@Valid @RequestBody ContactDto contactDto) {
        return contactService.createDto(contactDto);
    }
    
    @PatchMapping("/{id}")
    public ContactDto updateContact(@Valid @RequestBody ContactDto contactDto, @PathVariable Long id) {
        return contactService.updateDto(contactDto, id);
    }
}
