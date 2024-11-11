package uz.fluxCrm.fluxCrm.crm.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.LeadDto;
import uz.fluxCrm.fluxCrm.crm.dto.PipelineDto;
import uz.fluxCrm.fluxCrm.crm.dto.StatusDto;
import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.service.LeadService;
import uz.fluxCrm.fluxCrm.crm.service.PipelineService;
import uz.fluxCrm.fluxCrm.crm.service.StatusService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/leads")
@AllArgsConstructor
public class LeadsController {
	private final LeadService leadService;
	private final PipelineService pipelineService;
	private final StatusService statusService;


	@GetMapping({"", "/"})
	public Page<LeadDto> getLeads(Pageable pageable) {
		return leadService.getLeadsDto(pageable);
	}

	@GetMapping("/{id}")
	public LeadDto getLead(@PathVariable Long id) {
		return leadService.findByIdDto(id);
	}

	@PostMapping({"", "/"})
	@ResponseStatus(HttpStatus.CREATED)
	public LeadDto createLead(@Valid @RequestBody LeadDto leadDto) {		
		return leadService.createLeadDto(leadDto);
	}

	@GetMapping("/pipelines")
	public List<PipelineDto> getPipelines() {
		return pipelineService.getPipelinesDto();
	}
	
	@GetMapping("/pipelines/{pipelineId}")
	public PipelineDto getPipelineById(@PathVariable Long pipelineId) {
		return pipelineService.findByIdDto(pipelineId);
	}
	
	@GetMapping("/pipelines/{pipelineId}/statuses")
	public List<StatusDto> getStatuses(@PathVariable Long pipelineId) {
		Pipeline pipeline = pipelineService.findById(pipelineId);
		return statusService.getStatusesDto(pipeline);
	}
	
	@GetMapping("/pipelines/{pipelineId}/statuses/{statusId}")
	public StatusDto getStatusById(@PathVariable Long pipelineId, @PathVariable Long statusId) {
		Pipeline pipeline = pipelineService.findById(pipelineId);
		return statusService.getStatusDto(pipeline, statusId);
	}
}
