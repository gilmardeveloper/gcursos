package com.gilmarcarlos.developer.gcursos.security.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.Usuario;
import com.gilmarcarlos.developer.gcursos.security.token.AutenticaRegistroUsuario;

@Component
public class AutenticaUsuarioListener implements ApplicationListener<AutenticaUsuario>{
	
	@Autowired
	private AutenticaRegistroUsuario autenticador;
	
	@Override
	public void onApplicationEvent(AutenticaUsuario autentica) {
		
		Usuario usuario = autentica.getUsuario();
        autenticador.criarVerificacao(usuario);
		
	}

	
}
