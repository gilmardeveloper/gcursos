package com.gilmarcarlos.developer.gcursos.model.eventos.exceptions;

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
