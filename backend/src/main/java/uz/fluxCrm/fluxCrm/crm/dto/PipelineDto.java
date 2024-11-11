package uz.fluxCrm.fluxCrm.crm.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineDto {
    private Long id;

    private String name;

    private List<StatusDto> statuses;
}
