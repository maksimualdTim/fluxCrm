package uz.fluxCrm.fluxCrm.crm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;
import uz.fluxCrm.fluxCrm.crm.service.ContactService;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@WebMvcTest(ContactsController.class)
public class ContactsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void startUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void getContacts_shouldReturnPagedCompanies() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<ContactDto> contacts = List.of(TestObjectFactory.createContactDto());
        Page<ContactDto> contactPage = new PageImpl<>(contacts, pageable, contacts.size());

        when(contactService.getContactsDto(any(Pageable.class))).thenReturn(contactPage);

        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Test Contact"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void getContact_shouldReturnCompany() throws Exception {
        ContactDto contactDto = TestObjectFactory.createContactDto();

        when(contactService.findByIdDto(1L)).thenReturn(contactDto);

        mockMvc.perform(get("/contacts/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Contact"));
    }

    @Test
    void createContact_shouldCreateCompany() throws Exception {
        ContactDto createdContactDto = TestObjectFactory.createContactDto();

        when(contactService.createDto(any(ContactDto.class))).thenReturn(createdContactDto);

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdContactDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Contact"));
    }

    @Test
    void updateContact_shouldUpdateCompany() throws Exception {
        ContactDto updatedContactDto = TestObjectFactory.createContactDto();

        when(contactService.updateDto(any(ContactDto.class), anyLong())).thenReturn(updatedContactDto);

        mockMvc.perform(patch("/contacts/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedContactDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Contact"));
    }
}
