package com.template.demo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collections;
import java.util.List;
import lombok.*;

@Data
public class ExceptionResponse {

	private final String errorMessage;
	private final String requestedURI;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<ValidationError> validationErrors;

	public ExceptionResponse(final String errorMessage, final String requestedURI) {
		this(errorMessage, requestedURI, Collections.emptyList());
	}

	public ExceptionResponse(final String errorMessage, final String requestedURI,
			final List<ValidationError> validationErrors) {
		this.errorMessage = errorMessage;
		this.requestedURI = requestedURI;
		this.validationErrors = validationErrors;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ValidationError {
		private String field;
		private String validationError;
	}
}
