package uz.fluxCrm.fluxCrm.crm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDto {
    private Long id;

    private String name;

    private List<LeadDtoSimple> leads;
    
    private CompanyDto company;

    @JsonProperty("created_at")
    @PastOrPresent(message = "The created_at date must be in the past or present")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @PastOrPresent(message = "The updated_at date must be in the past or present")
    private LocalDateTime updatedAt;
}
