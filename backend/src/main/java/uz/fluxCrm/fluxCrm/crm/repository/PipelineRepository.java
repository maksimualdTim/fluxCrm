package uz.fluxCrm.fluxCrm.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.fluxCrm.fluxCrm.crm.entity.Pipeline;

public interface PipelineRepository extends JpaRepository<Pipeline, Long>{
    public Optional<Pipeline> findByIsMainTrue();
}
