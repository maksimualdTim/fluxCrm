package uz.fluxCrm.fluxCrm.crm.validation;


import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import uz.fluxCrm.fluxCrm.crm.repository.StatusRepository;

@Component
public class StatusExistsValidator implements ConstraintValidator<StatusExists, Long> {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public boolean isValid(Long statusId, ConstraintValidatorContext context) {
        if (statusId == null) {
            return false;
        }
        return statusRepository.existsById(statusId);
    }
}
