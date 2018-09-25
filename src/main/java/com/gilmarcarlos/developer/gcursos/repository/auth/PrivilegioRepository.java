package com.gilmarcarlos.developer.gcursos.repository.auth;

import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.auth.Privilegio;

/**
 * Interface para crud da entidade (Privilegio)
 * 
 * @author Gilmar Carlos
 *
 */
public interface PrivilegioRepository extends CrudRepository<Privilegio, Long> {

	Privilegio findByNome(String nome);

}
