package com.template.demo.validation.validators;

import com.template.demo.model.DepartmentRequestDTO;
import com.template.demo.validation.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DepartmentValidator implements ConstraintValidator<DepartmentValid, DepartmentRequestDTO> {

	@Override
	public boolean isValid(final DepartmentRequestDTO departmentRequestDTO, final ConstraintValidatorContext context) {

		final String name = departmentRequestDTO.getName();

		if (name.contains("test")) {
			ValidationUtil.addFormattedConstraintViolationToContext(context, "Name cannot contain the test phrase");
			return false;
		}

		return true;
	}

}
