package com.gilmarcarlos.developer.gcursos.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencial;

public interface ImagensEventoPresencialRepository extends CrudRepository<ImagensEventoPresencial, Long> {
	
	@Query("select i from ImagensEventoPresencial i where i.id = :pid")
	ImagensEventoPresencial buscarPor(@Param("pid") Long id);

}
