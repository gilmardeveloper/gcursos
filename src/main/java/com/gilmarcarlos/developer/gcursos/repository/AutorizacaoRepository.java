package com.gilmarcarlos.developer.gcursos.repository;

import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.Autorizacao;

public interface AutorizacaoRepository extends CrudRepository<Autorizacao, Long>{

	Autorizacao findByNome(String nome);

}
