package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

/**
 * Classe para lançar excessões na validação de unidades de trabalho
 * 
 * @author Gilmar Carlos
 *
 */
public class UnidadeNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnidadeNotFoundException() {
		super("unidade não existe");
	}
	
	public UnidadeNotFoundException(String msg) {
		super(msg);
	}

}
