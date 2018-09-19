package com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions;

public class SexoExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SexoExisteException() {
		super("opção não pode ser deletada");
	}
	
	public SexoExisteException(String msg) {
		super(msg);
	}

}
