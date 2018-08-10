package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUnidade;

public interface TelefoneUnidadeRepository extends CrudRepository<TelefoneUnidade, Long> {
	
	@Query("select t from TelefoneUnidade t")
	List<TelefoneUnidade> listAll();

}
