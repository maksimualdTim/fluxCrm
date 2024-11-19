package uz.fluxCrm.fluxCrm.crm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineDto {
    private Long id;

    @JsonProperty("is_main")
    private boolean isMain;

    @NotBlank
    private String name;

    private List<StatusDto> statuses;
}
