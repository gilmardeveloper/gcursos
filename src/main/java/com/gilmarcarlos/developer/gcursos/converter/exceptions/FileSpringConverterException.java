package com.gilmarcarlos.developer.gcursos.converter.exceptions;

public class FileSpringConverterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileSpringConverterException() {
		super("Uma imagem vazia ou outro arquivo não pode ser enviado");
	}
	
	public FileSpringConverterException(String msg) {
		super(msg);
	}

}
