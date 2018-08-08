package com.gilmarcarlos.developer.gcursos.security.exception;

import org.springframework.security.core.AuthenticationException;

public class BruteForceException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	
	public BruteForceException() {
		this("Favor aguarde 15 minutos até a próxima tentativa");
	}
	
	public BruteForceException(String msg) {
		super(msg);
	}
	
	
	
}
