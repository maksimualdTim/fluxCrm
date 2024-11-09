package uz.fluxCrm.fluxCrm.crm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leads")
public class LeadsController {
	
	@GetMapping({"", "/"})
	public String getLeads() {
		return "Hello from Spring boot2";
	}
}
