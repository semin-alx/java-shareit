package ru.practicum.shareit.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= NullOrNotEmptyValidator.class)
public @interface NullOrNotEmptyConstraint {
    String message() default "Значение не должно быть пустым или должно быть null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
