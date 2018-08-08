package com.gilmarcarlos.developer.gcursos.security.exception;

import org.springframework.security.core.AuthenticationException;

public class RegistroNotEnableException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	
	public RegistroNotEnableException() {
		this("Está conta aidna não foi habilitada");
	}
	
	public RegistroNotEnableException(String msg) {
		super(msg);
	}
	
	
	
}
