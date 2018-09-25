package com.gilmarcarlos.developer.gcursos.security.token;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Interface com default metodos para implementação de autenticadores de usuarios 
 * 
 * @author Gilmar Carlos
 *
 */
public interface AutenticaToken {
	
	/**
	 * Método que criar um nova verificação para um registro de um novo usuario
	 * 
	 * @param usuario
	 * 
	 */
	void criarVerificacao(Usuario usuario);
	
	/**
	 * Método que habilita um novo registro de um usuario
	 * 
	 * @param usuario
	 * 
	 */
	void habilitar(Usuario usuario);
	
}
