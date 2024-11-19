package uz.fluxCrm.fluxCrm.crm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import uz.fluxCrm.fluxCrm.crm.dto.ContactDto;
import uz.fluxCrm.fluxCrm.crm.dto.ContactDtoSimple;
import uz.fluxCrm.fluxCrm.crm.entity.Contact;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactMapper {

    public ContactDto toResponse(Contact contact);

    public ContactDtoSimple toSimpleResponse(Contact contact);

    @Mapping(target = "id", ignore = true)
    public Contact toEntity(ContactDto contactDto);

    @Mapping(target = "id", ignore = true)
    public Contact toEntity(ContactDtoSimple contactDto);
}
