package com.template.demo.validation.utils;

import java.text.MessageFormat;
import javax.validation.ConstraintValidatorContext;

public class ValidationUtil {

	private ValidationUtil() {
	}

	public static void addFormattedConstraintViolationToContext(final ConstraintValidatorContext context,
			final String templateString, final String... values) {
		addConstraintViolationToContext(context, formattedString(templateString, values));
	}

	private static void addConstraintViolationToContext(final ConstraintValidatorContext context,
			final String templateString) {
		context.buildConstraintViolationWithTemplate(templateString).addConstraintViolation()
				.disableDefaultConstraintViolation();
	}

	private static String formattedString(final String preformattedString, String... values) {
		return MessageFormat.format(preformattedString, (Object[]) values);
	}
}
