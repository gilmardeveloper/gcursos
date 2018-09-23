package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

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
