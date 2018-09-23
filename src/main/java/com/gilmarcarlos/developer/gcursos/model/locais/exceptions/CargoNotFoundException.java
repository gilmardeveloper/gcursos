package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

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
