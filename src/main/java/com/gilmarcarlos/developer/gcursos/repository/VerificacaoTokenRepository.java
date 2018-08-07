package com.gilmarcarlos.developer.gcursos.repository;

import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.Usuario;
import com.gilmarcarlos.developer.gcursos.model.UsuarioToken;

public interface VerificacaoTokenRepository extends CrudRepository<UsuarioToken, Long>{
	
	UsuarioToken findByToken(String token);
    UsuarioToken findByUsuario(Usuario usuario);

}
