package com.gilmarcarlos.developer.gcursos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.gilmarcarlos.developer.gcursos.model.Usuario;

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
