package uz.fluxCrm.fluxCrm.crm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import uz.fluxCrm.fluxCrm.crm.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
    Page<Company> findAll(Pageable pageable);
}
