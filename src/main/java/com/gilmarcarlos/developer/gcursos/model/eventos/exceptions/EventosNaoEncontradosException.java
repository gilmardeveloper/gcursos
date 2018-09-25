package com.gilmarcarlos.developer.gcursos.model.eventos.exceptions;

/**
 * Classe para lançar excessões na validação de eventos
 * 
 * @author Gilmar Carlos
 *
 */
public class EventosNaoEncontradosException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EventosNaoEncontradosException() {
		super("nenhum evento foi encontrado para este periodo");
	}
	
	public EventosNaoEncontradosException(String msg) {
		super(msg);
	}

}
