package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.EnderecoUnidade;

public interface EnderecoUnidadeRepository extends CrudRepository<EnderecoUnidade, Long> {
	
	@Query("select e from EnderecoUnidade e")
	List<EnderecoUnidade> listAll();

}
