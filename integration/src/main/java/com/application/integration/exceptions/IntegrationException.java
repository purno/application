package com.application.integration.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IntegrationException extends Exception {

	private static final long serialVersionUID = 1L;
	

	public IntegrationException(final String message) {
		super(message);
	}
	
	public IntegrationException(Throwable cause) {
		super(cause);
	}
	
	public IntegrationException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
