package com.gilmarcarlos.developer.gcursos.model.locais.exceptions;

/**
 * Classe para lançar excessões na validação de departamentos
 * 
 * @author Gilmar Carlos
 *
 */
public class DepartamentoNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DepartamentoNotFoundException() {
		super("departamento não existe");
	}
	
	public DepartamentoNotFoundException(String msg) {
		super(msg);
	}

}
