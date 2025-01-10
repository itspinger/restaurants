package raf.rs.restaurants.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AlphanumericValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alphanumeric {
    String message() default "Field must be alphanumeric";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}