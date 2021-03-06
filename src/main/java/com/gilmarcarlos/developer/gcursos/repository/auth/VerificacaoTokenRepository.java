package com.gilmarcarlos.developer.gcursos.repository.auth;

import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.auth.UsuarioToken;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

/**
 * Interface para crud da entidade (UsuarioToken)
 * 
 * @author Gilmar Carlos
 *
 */
public interface VerificacaoTokenRepository extends CrudRepository<UsuarioToken, Long>{
	
	UsuarioToken findByToken(String token);
    UsuarioToken findByUsuario(Usuario usuario);

}
