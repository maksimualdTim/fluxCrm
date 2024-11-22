package uz.fluxCrm.fluxCrm.crm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import uz.fluxCrm.fluxCrm.crm.entity.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long>{
    Page<Lead> findAll(Pageable pageable);
}
