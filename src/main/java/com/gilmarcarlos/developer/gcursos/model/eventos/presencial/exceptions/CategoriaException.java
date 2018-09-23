package com.gilmarcarlos.developer.gcursos.model.eventos.presencial.exceptions;

public class CategoriaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CategoriaException() {
		super("ocorreu um erro");
	}
	
	public CategoriaException(String msg) {
		super(msg);
	}

}
