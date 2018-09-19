package com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions;

public class UsuarioDeleteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UsuarioDeleteException() {
		super("usuário não pode ser deletado");
	}
	
	public UsuarioDeleteException(String msg) {
		super(msg);
	}

}
