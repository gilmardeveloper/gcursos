package com.gilmarcarlos.developer.gcursos.security.reset.passwd;

import org.springframework.context.ApplicationEvent;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Classe auxiliar para disparo de eventos de callback para criar solicitações para redefinição
 * de senhas
 * 
 * @author Gilmar Carlos
 *
 */
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
