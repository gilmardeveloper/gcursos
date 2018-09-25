package com.gilmarcarlos.developer.gcursos.model.eventos.exceptions;

/**
 * Classe para lançar excessões na validação de eventos
 * 
 * @author Gilmar Carlos
 *
 */
public class DeleteEventoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DeleteEventoException() {
		super("esse evetno não pode ser deletado");
	}
	
	public DeleteEventoException(String msg) {
		super(msg);
	}

}
