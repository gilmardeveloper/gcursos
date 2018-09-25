package com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions;

/**
 * Classe para lançar excessões na validação de usuarios
 * 
 * @author Gilmar Carlos
 *
 */
public class CpfExisteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CpfExisteException() {
		super("usuário já existe");
	}
	
	public CpfExisteException(String msg) {
		super(msg);
	}

}
