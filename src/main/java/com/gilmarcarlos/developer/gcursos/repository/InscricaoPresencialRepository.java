package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.InscricaoPresencial;

public interface InscricaoPresencialRepository extends CrudRepository<InscricaoPresencial, Long> {
	
	@Query("select i from InscricaoPresencial i")
	List<InscricaoPresencial> listAll();
	
	@Query("select i from InscricaoPresencial i where i.id = :pid")
	InscricaoPresencial buscarPor(@Param("pid") Long id);

}
