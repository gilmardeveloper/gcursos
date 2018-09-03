package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.PermissoesEventoPresencial;

public interface PermissoesEventoPresencialRepository extends CrudRepository<PermissoesEventoPresencial, Long> {
	
	@Query("select p from PermissoesEventoPresencial p")
	List<PermissoesEventoPresencial> listAll();
	
	@Query("select p from PermissoesEventoPresencial p where p.id = :pid")
	PermissoesEventoPresencial buscarPor(@Param("pid") Long id);

}
