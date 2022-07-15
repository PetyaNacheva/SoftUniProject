package MyProjectGradle.models.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface ValidationDates {
    String message() default "Arrival date must be earlier that leave date";

    String first();

    String second();


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
