package com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions;

public class UsuarioExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UsuarioExisteException() {
		super("usuário já existe");
	}
	
	public UsuarioExisteException(String msg) {
		super(msg);
	}

}
