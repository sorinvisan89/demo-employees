package com.template.demo.validation.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DepartmentValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DepartmentValid {

	String message() default "Not a valid Department";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
