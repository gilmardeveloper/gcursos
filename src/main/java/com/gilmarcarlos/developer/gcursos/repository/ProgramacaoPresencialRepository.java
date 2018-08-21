package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.ProgramacaoPresencial;

public interface ProgramacaoPresencialRepository extends CrudRepository<ProgramacaoPresencial, Long> {
	
	@Query("select p from ProgramacaoPresencial p join fetch p.dias")
	List<ProgramacaoPresencial> listAll();
	
	@Query("select p from ProgramacaoPresencial p join fetch p.dias where p.id = :pid")
	ProgramacaoPresencial buscarPor(@Param("pid") Long id);

}
