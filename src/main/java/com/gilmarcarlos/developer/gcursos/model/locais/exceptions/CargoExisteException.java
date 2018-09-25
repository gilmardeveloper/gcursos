package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

/**
 * Classe para lançar excessões na validação de cargos
 * 
 * @author Gilmar Carlos
 *
 */
public class CargoExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CargoExisteException() {
		super("cargo existe");
	}
	
	public CargoExisteException(String msg) {
		super(msg);
	}

}
