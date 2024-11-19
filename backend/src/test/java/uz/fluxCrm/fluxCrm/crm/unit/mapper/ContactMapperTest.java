package uz.fluxCrm.fluxCrm.crm.unit.mapper;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import uz.fluxCrm.fluxCrm.crm.mapper.CompanyMapper;
import uz.fluxCrm.fluxCrm.crm.mapper.ContactMapperImpl;
import uz.fluxCrm.fluxCrm.crm.mapper.LeadMapper;

@ExtendWith(MockitoExtension.class)
public class ContactMapperTest {
    @Mock
    private LeadMapper leadMapper;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private ContactMapperImpl contactMapper;

    
}
