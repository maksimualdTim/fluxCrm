package uz.fluxCrm.fluxCrm.crm.unit.mapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uz.fluxCrm.fluxCrm.crm.dto.CompanyDto;
import uz.fluxCrm.fluxCrm.crm.dto.CompanyDtoSimple;
import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;
import uz.fluxCrm.fluxCrm.crm.dto.ContactDtoSimple;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.entity.Company;
import uz.fluxCrm.fluxCrm.crm.entity.Contact;
import uz.fluxCrm.fluxCrm.crm.entity.Lead;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.entity.Status;
import uz.fluxCrm.fluxCrm.crm.mapper.CompanyMapper;
import uz.fluxCrm.fluxCrm.crm.mapper.ContactMapper;
import uz.fluxCrm.fluxCrm.crm.mapper.LeadMapperImpl;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;
import uz.fluxCrm.fluxCrm.crm.util.TestObjectFactory;

@ExtendWith(MockitoExtension.class)
public class LeadMapperTest {

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private LeadMapperImpl leadMapper;

    private Lead lead;

    @BeforeEach
    void setUp() {
        Pipeline pipeline = TestObjectFactory.createPipeline();
        pipeline.setId(1L);
        Status status = TestObjectFactory.createStatus();
        status.setPipeline(pipeline);
        status.setId(1L);
        pipeline.setStatuses(List.of(status));

        Company company = new Company();
        company.setId(1L);
        company.setName("Test Company");

        lead = new Lead();
        lead.setId(1L);
        lead.setName("Test Lead");
        lead.setPrice(1000L);
        lead.setStatus(status);
        lead.setCreatedAt(LocalDateTime.of(2024, 9, 10, 20, 32, 18));
        lead.setUpdatedAt(LocalDateTime.of(2024, 9, 10, 20, 32, 18));
        lead.setCompany(company);

        company.setLeads(List.of(lead));

        Contact contact = new Contact();
        contact.setCompany(company);
        contact.setId(1L);
        contact.setLeads(List.of(lead));
        contact.setCreatedAt(LocalDateTime.of(2024, 9, 10, 20, 32, 18));
        contact.setName("Test Contact");
        lead.setContacts(List.of(contact));
    }

    @Test
    void testToResponse_ReturnsLeadDto() {
        CompanyDtoSimple companyDto = TestObjectFactory.createCompanyDtoSimple();
        ContactDtoSimple contactDto = TestObjectFactory.createContactDtoSimple();

        when(companyMapper.toSimpleResponse(any(Company.class))).thenReturn(companyDto);
        when(contactMapper.toSimpleResponse(any(Contact.class))).thenReturn(contactDto);

        LeadDto leadDto = leadMapper.toResponse(lead);

        assertNotNull(leadDto);
        assertEquals(1L, leadDto.getId());
        assertEquals(1L, leadDto.getStatusId());
        assertEquals("Test Lead", leadDto.getName());
        assertEquals(1L, leadDto.getPipelineId());
        assertEquals(1000L, leadDto.getPrice());
        assertEquals(LocalDateTime.of(2024, 9, 10, 20, 32, 18), leadDto.getCreatedAt());

        assertNotNull(leadDto.getCompany());
        assertEquals(1L, leadDto.getCompany().getId());
        assertEquals("Test Company", leadDto.getCompany().getName());
        assertNotNull(leadDto.getContacts());
        assertEquals("Test Contact", leadDto.getContacts().getFirst().getName());
        assertEquals(1L, leadDto.getContacts().getFirst().getId());
    }

    @Test
    void testToEntity_StatusExists_ReturnsLead() {
        CompanyDtoSimple companyDto = TestObjectFactory.createCompanyDtoSimple();
        Contact contact = TestObjectFactory.createContact();
        Company company = TestObjectFactory.createCompany();
        ContactDtoSimple contactDto = TestObjectFactory.createContactDtoSimple();

        LeadDto leadDto = TestObjectFactory.createTestLead();
        leadDto.setCompany(companyDto);
        leadDto.setContacts(List.of(contactDto));

        Status status = TestObjectFactory.createStatus();
        Optional<Status> statusOptional = Optional.of(status);

        when(contactMapper.toEntity(any(ContactDtoSimple.class))).thenReturn(contact);
        when(companyMapper.toEntity(any(CompanyDtoSimple.class))).thenReturn(company);
        when(statusRepository.findById(anyLong())).thenReturn(statusOptional);

        Lead lead = leadMapper.toEntity(leadDto, statusRepository);

        assertNotNull(lead);
        assertEquals("Test Lead", lead.getName());
        assertEquals(1000L, lead.getPrice());  
        assertNotNull(lead.getStatus());
        assertNotNull(lead.getCompany());
        assertNotNull(lead.getContacts());
        assertEquals("Test Company", lead.getCompany().getName());
    }
}
