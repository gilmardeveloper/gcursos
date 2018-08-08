package com.gilmarcarlos.developer.gcursos.security.reset.passwd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.Usuario;
import com.gilmarcarlos.developer.gcursos.security.token.AutenticaRegistroUsuario;

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
