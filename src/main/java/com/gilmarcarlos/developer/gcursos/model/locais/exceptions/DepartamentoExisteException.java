package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

public class DepartamentoExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DepartamentoExisteException() {
		super("departamento existe");
	}
	
	public DepartamentoExisteException(String msg) {
		super(msg);
	}

}
