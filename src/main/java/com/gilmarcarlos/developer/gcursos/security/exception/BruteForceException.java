package com.gilmarcarlos.developer.gcursos.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Classe para lançar excessões na validação de ataques de força bruta
 * 
 * @author Gilmar Carlos
 *
 */
public class BruteForceException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	
	public BruteForceException() {
		this("Favor aguarde 15 minutos até a próxima tentativa");
	}
	
	public BruteForceException(String msg) {
		super(msg);
	}
	
	
	
}
