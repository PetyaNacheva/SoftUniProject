package MyProjectGradle.models.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidationDates, Object> {
    private String firstField;
    private String secondField;
    private String message;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        Object firstFieldValue = beanWrapper.getPropertyValue(firstField);
        Object secondFieldValue = beanWrapper.getPropertyValue(secondField);

        boolean valid = false;
        if (firstFieldValue instanceof LocalDate && secondFieldValue instanceof LocalDate) {
            valid = ((LocalDate) firstFieldValue).isBefore((LocalDate) secondFieldValue);
        }
        if (!valid) {
            context.
                    buildConstraintViolationWithTemplate(message).
                    addPropertyNode(firstField).
                    addConstraintViolation().
                    buildConstraintViolationWithTemplate(message).
                    addPropertyNode(secondField).
                    addConstraintViolation().
                    disableDefaultConstraintViolation();
        }
        return valid;
    }

    @Override
    public void initialize(ValidationDates constraintAnnotation) {
        this.firstField = constraintAnnotation.first();
        this.secondField = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }
}
