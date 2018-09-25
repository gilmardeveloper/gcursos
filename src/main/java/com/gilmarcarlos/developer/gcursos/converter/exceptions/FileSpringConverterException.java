package com.gilmarcarlos.developer.gcursos.converter.exceptions;

/**
 * Classe para lançar excessões na falha de converções de arquivos
 * 
 * @author Gilmar Carlos
 *
 */
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
