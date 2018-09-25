package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

/**
 * Classe para lançar excessões na validação de unidades de trabalho
 * 
 * @author Gilmar Carlos
 *
 */
public class UnidadeExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnidadeExisteException() {
		super("unidade existe");
	}
	
	public UnidadeExisteException(String msg) {
		super(msg);
	}

}
