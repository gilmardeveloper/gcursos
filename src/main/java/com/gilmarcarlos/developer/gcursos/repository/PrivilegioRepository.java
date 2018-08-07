package com.gilmarcarlos.developer.gcursos.repository;

import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.Privilegio;

public interface PrivilegioRepository extends CrudRepository<Privilegio, Long> {

	Privilegio findByNome(String nome);

}
