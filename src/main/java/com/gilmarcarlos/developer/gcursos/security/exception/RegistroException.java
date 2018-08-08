package com.gilmarcarlos.developer.gcursos.security.exception;

import org.springframework.security.core.AuthenticationException;

public class RegistroException extends AuthenticationException{

	public RegistroException() {
		this("Você já é cadastrado");
	}
	
	public RegistroException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
