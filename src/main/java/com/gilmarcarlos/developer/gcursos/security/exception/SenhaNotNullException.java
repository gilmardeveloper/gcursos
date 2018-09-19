package com.gilmarcarlos.developer.gcursos.security.exception;

public class SenhaNotNullException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public SenhaNotNullException() {
		this("a senha não pode ser vazia");
	}
	
	public SenhaNotNullException(String msg) {
		super(msg);
	}
	
	
	
}
