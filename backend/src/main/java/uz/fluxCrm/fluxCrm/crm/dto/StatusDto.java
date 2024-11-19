package uz.fluxCrm.fluxCrm.crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDto {
    private Long id;

    @NotBlank
    private String name;

    @JsonProperty("is_main")
    private boolean isMain;

    @JsonProperty("pipeline_id")
    private Long pipelineId;
}
