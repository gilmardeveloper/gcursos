package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;

public interface AtividadePresencialRepository extends CrudRepository<AtividadePresencial, Long> {
	
	@Query("select a from AtividadePresencial a")
	List<AtividadePresencial> listAll();
	
	@Query("select a from AtividadePresencial a where a.id = :pid")
	AtividadePresencial buscarPor(@Param("pid") Long id);

}
