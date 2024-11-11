package uz.fluxCrm.fluxCrm.crm.dto;

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

}
