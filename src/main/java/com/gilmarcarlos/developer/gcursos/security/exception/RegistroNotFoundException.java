package com.gilmarcarlos.developer.gcursos.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Classe para lançar excessões na validação de registros
 * 
 * @author Gilmar Carlos
 *
 */
public class RegistroNotFoundException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	
	public RegistroNotFoundException() {
		this("Nenhuma conta foi encontrado com esses dados");
	}
	
	public RegistroNotFoundException(String msg) {
		super(msg);
	}
	
	
	
}
