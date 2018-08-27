package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;

public interface EventoPresencialRepository extends CrudRepository<EventoPresencial, Long> {
	
	@Query("select e from EventoPresencial e")
	List<EventoPresencial> listAll();
	
	@Query("select e from EventoPresencial e where e.id = :pid")
	EventoPresencial buscarPor(@Param("pid")Long id);

	List<EventoPresencial> findByPublicadoTrue();

}
