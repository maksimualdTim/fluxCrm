package uz.fluxCrm.fluxCrm.crm.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineDto {
    private Long id;

    @NotBlank
    private String name;

    private List<StatusDto> statuses;
}
