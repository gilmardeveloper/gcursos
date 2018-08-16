package com.gilmarcarlos.developer.gcursos.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.Imagens;

public interface ImagensRepository extends CrudRepository<Imagens, Long> {
	
	@Query("select i from Imagens i where i.id = :pid")
	Imagens buscarPor(@Param("pid") Long id);

}
