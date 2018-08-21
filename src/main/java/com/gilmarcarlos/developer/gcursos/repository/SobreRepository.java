package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.Sobre;

public interface SobreRepository extends CrudRepository<Sobre, Long> {
	
	@Query("select c from Sobre c")
	List<Sobre> listAll();
	
	@Query("select c from Sobre c where c.id = :pid")
	Sobre buscarPor(@Param("pid") Long id);

}
