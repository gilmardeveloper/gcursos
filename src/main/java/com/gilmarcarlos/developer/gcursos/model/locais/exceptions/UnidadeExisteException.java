package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

public class UnidadeExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnidadeExisteException() {
		super("unidade existe");
	}
	
	public UnidadeExisteException(String msg) {
		super(msg);
	}

}
