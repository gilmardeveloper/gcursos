package com.gilmarcarlos.developer.gcursos.security.auth.user;

import org.springframework.context.ApplicationEvent;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Classe auxiliar para disparar eventos de autenticação de registros
 * 
 * @author Gilmar Carlos
 *
 */
public class AutenticaUsuario extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;

	public AutenticaUsuario(Usuario usuario) {
		super(usuario);
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	
}
