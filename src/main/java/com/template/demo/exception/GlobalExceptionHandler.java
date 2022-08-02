package com.template.demo.exception;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MissingEntityException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ExceptionResponse handleNotFoundException(final MissingEntityException ex,
			final HttpServletRequest request) {
		logError(ex);
		return new ExceptionResponse(ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler({IllegalArgumentException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionResponse handleBadRequest(final IllegalArgumentException ex, final HttpServletRequest request) {
		logError(ex);
		return new ExceptionResponse(ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ExceptionResponse handleInternalServerError(final RuntimeException ex, final HttpServletRequest request) {
		logError(ex);
		return new ExceptionResponse(ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ExceptionResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
			final HttpServletRequest request) {
		logError(ex);

		final List<ExceptionResponse.ValidationError> validationErrors = ex
				.getBindingResult().getFieldErrors().stream().map(error -> ExceptionResponse.ValidationError.builder()
						.field(error.getField()).validationError(error.getDefaultMessage()).build())
				.collect(Collectors.toList());

		return new ExceptionResponse("A validation error occurred", request.getRequestURI(), validationErrors);
	}

	private void logError(final Exception ex) {
		log.error("Failed processing the incoming request", ex);
	}
}
