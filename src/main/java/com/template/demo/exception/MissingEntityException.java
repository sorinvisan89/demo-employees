package com.template.demo.exception;

public class MissingEntityException extends RuntimeException {

	public MissingEntityException(final String message) {
		super(message);
	}
}
