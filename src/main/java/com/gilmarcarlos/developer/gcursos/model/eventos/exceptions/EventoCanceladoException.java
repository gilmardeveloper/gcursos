package com.gilmarcarlos.developer.gcursos.model.eventos.exceptions;

public class EventoCanceladoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EventoCanceladoException() {
		super("este evento está cancelado");
	}
	
	public EventoCanceladoException(String msg) {
		super(msg);
	}

}
