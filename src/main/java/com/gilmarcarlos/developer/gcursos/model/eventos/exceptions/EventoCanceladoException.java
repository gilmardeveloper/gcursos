package com.gilmarcarlos.developer.gcursos.model.eventos.exceptions;

/**
 * Classe para lançar excessões na validação de eventos
 * 
 * @author Gilmar Carlos
 *
 */
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
