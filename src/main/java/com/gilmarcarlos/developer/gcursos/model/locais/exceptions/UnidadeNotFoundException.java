package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

public class UnidadeNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnidadeNotFoundException() {
		super("unidade não existe");
	}
	
	public UnidadeNotFoundException(String msg) {
		super(msg);
	}

}
