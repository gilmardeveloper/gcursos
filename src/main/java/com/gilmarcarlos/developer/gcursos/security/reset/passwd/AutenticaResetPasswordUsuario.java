package com.gilmarcarlos.developer.gcursos.security.reset.passwd;

import org.springframework.context.ApplicationEvent;

import com.gilmarcarlos.developer.gcursos.model.Usuario;

public class AutenticaResetPasswordUsuario extends ApplicationEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	
	public AutenticaResetPasswordUsuario(Usuario usuario) {
		super(usuario);
		this.usuario = usuario;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
}
