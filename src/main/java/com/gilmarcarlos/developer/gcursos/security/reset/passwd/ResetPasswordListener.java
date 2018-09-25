package com.gilmarcarlos.developer.gcursos.security.reset.passwd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.security.token.AutenticaRegistroUsuario;

/**
 * Classe listenner que dispara um evento de callback da classe (AutenticaResetPasswordUsuario)
 * 
 * @author Gilmar Carlos
 *
 */
@Component
public class ResetPasswordListener implements ApplicationListener<AutenticaResetPasswordUsuario>{

	@Autowired
	private AutenticaRegistroUsuario autenticador;
	
	@Override
	public void onApplicationEvent(AutenticaResetPasswordUsuario autentica) {
		Usuario usuario = autentica.getUsuario();
        autenticador.resetPassword(usuario);		
	}

}
