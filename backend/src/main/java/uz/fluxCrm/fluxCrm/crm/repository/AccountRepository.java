package uz.fluxCrm.fluxCrm.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.fluxCrm.fluxCrm.crm.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

}
