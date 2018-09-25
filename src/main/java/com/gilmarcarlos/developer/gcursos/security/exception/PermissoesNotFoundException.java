package com.gilmarcarlos.developer.gcursos.security.exception;

/**
 * Classe para lançar excessões na validação de permissões de usuários
 * 
 * @author Gilmar Carlos
 *
 */
public class PermissoesNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PermissoesNotFoundException() {
		super("usuário não tem permissão para essa ação");
	}
	
	public PermissoesNotFoundException(String msg) {
		super(msg);
	}

}
