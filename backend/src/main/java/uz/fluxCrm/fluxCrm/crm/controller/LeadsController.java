package uz.fluxCrm.fluxCrm.crm.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/leads")
@AllArgsConstructor
public class LeadsController {
	private final LeadService leadService;
	private final PipelineService pipelineService;


	@GetMapping({"", "/"})
	public Page<LeadDto> getLeads(Pageable pageable) {
		return leadService.getLeadsDto(pageable);
	}

	@GetMapping("/{id}")
	public LeadDto getLead(@PathVariable Long id) {
		return leadService.findByIdDto(id);
	}

	@PatchMapping("/{id}")
	public LeadDto updateLead(@PathVariable Long id, @Valid LeadDto leadDto) {
		return leadService.updateDto(leadDto, id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLead(@PathVariable Long id) {
		leadService.delete(id);
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
	
	@PostMapping("/pipelines")
	@ResponseStatus(HttpStatus.CREATED)
	public PipelineDto createPipeline(@Valid @RequestBody PipelineDto pipelineDto) {
		return pipelineService.createPipelineDto(pipelineDto.getName());
	}

	@GetMapping("/pipelines/{pipelineId}")
	public PipelineDto getPipelineById(@PathVariable Long pipelineId) {
		return pipelineService.findByIdDto(pipelineId);
	}

	@PatchMapping("/pipelines/{pipelineId}")
	public PipelineDto updatePipeline(@PathVariable Long pipelineId, @Valid @RequestBody PipelineDto pipelineDto) {
		return pipelineService.updatePipelineDto(pipelineDto.getName(), pipelineId);
	}

	@DeleteMapping("/pipelines/{pipelineId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePipeline(@PathVariable Long pipelineId) {
		pipelineService.deletePipeline(pipelineId);
	}
	
	@GetMapping("/pipelines/{pipelineId}/statuses")
	public List<StatusDto> getStatuses(@PathVariable Long pipelineId) {
		Pipeline pipeline = pipelineService.findById(pipelineId);
		return pipelineService.getStatusesDto(pipeline);
	}

	@PostMapping("/pipelines/{pipelineId}/statuses")
	@ResponseStatus(HttpStatus.CREATED)
	public StatusDto createStatus(@PathVariable Long pipelineId, @Valid @RequestBody StatusDto statusDto) {
		return pipelineService.createStatusDto(statusDto.getName(), pipelineId);
	}
	
	@GetMapping("/pipelines/{pipelineId}/statuses/{statusId}")
	public StatusDto getStatusById(@PathVariable Long pipelineId, @PathVariable Long statusId) {
		Pipeline pipeline = pipelineService.findById(pipelineId);
		return pipelineService.getStatusDto(pipeline, statusId);
	}

	@PatchMapping("/pipelines/{pipelineId}/statuses/{statusId}")
	public StatusDto updateStatus(@PathVariable Long pipelineId, @PathVariable Long statusId, @Valid @RequestBody StatusDto statusDto) {
		return pipelineService.updateStatusDto(statusDto.getName(), pipelineId, statusId);
	}

	@DeleteMapping("/pipelines/{pipelineId}/statuses/{statusId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteStatus(@PathVariable Long pipelineId, @PathVariable Long statusId) {
		pipelineService.deleteStatus(pipelineId, statusId);
	}
}
