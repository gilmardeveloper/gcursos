package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnline;

public interface InscricaoOnlineRepository extends CrudRepository<InscricaoOnline, Long> {
	
	@Query("select i from InscricaoOnline i")
	List<InscricaoOnline> listAll();
	
	@Query("select i from InscricaoOnline i where i.id = :pid")
	InscricaoOnline buscarPor(@Param("pid") Long id);
	
}
