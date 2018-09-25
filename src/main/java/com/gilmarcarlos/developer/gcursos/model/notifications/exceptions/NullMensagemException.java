package com.gilmarcarlos.developer.gcursos.model.notifications.exceptions;

/**
 * Classe que lança excessões na validações de mensagens
 * 
 * @author Gilmar Carlos
 *
 */
public class NullMensagemException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NullMensagemException() {
		super("você precisa preencher todos os campos!");
	}
	
	public NullMensagemException(String msg) {
		super(msg);
	}

}
