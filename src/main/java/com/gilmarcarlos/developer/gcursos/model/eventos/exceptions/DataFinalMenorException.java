package com.gilmarcarlos.developer.gcursos.model.eventos.exceptions;

public class DataFinalMenorException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataFinalMenorException() {
		super("data final menor que data inicial");
	}
	
	public DataFinalMenorException(String msg) {
		super(msg);
	}

}
