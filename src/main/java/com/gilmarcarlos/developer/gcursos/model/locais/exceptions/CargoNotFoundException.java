package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

/**
 * Classe para lançar excessões na validação de cargos
 * 
 * @author Gilmar Carlos
 *
 */
public class CargoNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CargoNotFoundException() {
		super("cargo não existe");
	}
	
	public CargoNotFoundException(String msg) {
		super(msg);
	}

}
