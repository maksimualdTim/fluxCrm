package uz.fluxCrm.fluxCrm.crm.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uz.fluxCrm.fluxCrm.crm.validation.StatusExists;

@Getter
@Setter
public class LeadDto {
    private Long id;

    private String name;

    private Long price;

    @JsonProperty("pipeline_id")
    private Long pipelineId;

    @NotNull(message = "Status is mandatory")
    @StatusExists(message = "Status does not exists")
    @JsonProperty("status_id")
    private Long statusId;

    private List<ContactDtoSimple> contacts;

    private CompanyDtoSimple company;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}