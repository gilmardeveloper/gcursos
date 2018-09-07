package com.gilmarcarlos.developer.gcursos.model.eventos.online.exceptions;

public class PosicaoExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PosicaoExisteException() {
		super("atividades do mesmo módulo não podem ter a mesma posição");
	}
	
	public PosicaoExisteException(String msg) {
		super(msg);
	}

}
