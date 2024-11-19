package uz.fluxCrm.fluxCrm.crm.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import uz.fluxCrm.fluxCrm.crm.service.PipelineService;

@Configuration
@Profile("!test")
public class CrmConfig {
    @Bean
    CommandLineRunner initPipeline(PipelineService pipelineService) {
        return args -> {
			pipelineService.createPipeline("Воронка");
        };
    }	
}
