package uz.fluxCrm.fluxCrm.crm.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusExistsValidator.class)
@Documented
public @interface StatusExists {
    String message() default "Status does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
