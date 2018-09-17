package com.gilmarcarlos.developer.gcursos.security.exception;

public class PermissoesNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PermissoesNotFoundException() {
		super("usuário não tem permissão para essa ação");
	}
	
	public PermissoesNotFoundException(String msg) {
		super(msg);
	}

}
