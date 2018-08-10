package com.gilmarcarlos.developer.gcursos.security.token;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

public interface AutenticaToken {

	void criarVerificacao(Usuario usuario);
	void habilitar(Usuario usuario);
	
}
