package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;

public interface DadosPessoaisRepository extends CrudRepository<DadosPessoais, Long> {
	
	@Query("select u from DadosPessoais u")
	List<DadosPessoais> listAll();

}
