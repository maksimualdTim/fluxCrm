package uz.fluxCrm.fluxCrm.crm.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;
import uz.fluxCrm.fluxCrm.crm.service.PipelineService;
import uz.fluxCrm.fluxCrm.crm.service.StatusService;

@Configuration
@Profile("!test")
public class CrmConfig {
    @Bean
    CommandLineRunner initPipeline(PipelineService pipelineService, StatusService statusService) {
        return args -> {
			Pipeline pipeline = pipelineService.createDefault();
			statusService.createDefault(pipeline);
        };
    }	
}
