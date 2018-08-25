package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;

public interface AtividadePresencialRepository extends CrudRepository<AtividadePresencial, Long> {
	
	@Query("select a from AtividadePresencial a")
	List<AtividadePresencial> listAll();
	
	@Query("select a from AtividadePresencial a where a.diaEvento.programacaoPresencial.eventoPresencial.id = :pid order by a.horaInicio")
	List<AtividadePresencial> buscarPorEvento(@Param("pid") Long id);
	
	@Query("select a from AtividadePresencial a where a.id = :pid")
	AtividadePresencial buscarPor(@Param("pid") Long id);
	
	@Transactional
	@Modifying
	@Query("delete from AtividadePresencial a where a.diaEvento.id = :pid")
	void deletePorDia(@Param("pid") Long id);

}
