package com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions;

/**
 * Classe para lançar excessões na validação de escolaridades
 * 
 * @author Gilmar Carlos
 *
 */
public class EscolaridadeExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EscolaridadeExisteException() {
		super("opção não pode ser deletada");
	}
	
	public EscolaridadeExisteException(String msg) {
		super(msg);
	}

}
