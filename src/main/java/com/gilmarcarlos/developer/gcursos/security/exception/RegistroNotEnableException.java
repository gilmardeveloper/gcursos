package com.gilmarcarlos.developer.gcursos.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Classe para lançar excessões na validação de registros
 * 
 * @author Gilmar Carlos
 *
 */
public class RegistroNotEnableException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	
	public RegistroNotEnableException() {
		this("Está conta ainda não foi habilitada");
	}
	
	public RegistroNotEnableException(String msg) {
		super(msg);
	}
	
	
	
}
