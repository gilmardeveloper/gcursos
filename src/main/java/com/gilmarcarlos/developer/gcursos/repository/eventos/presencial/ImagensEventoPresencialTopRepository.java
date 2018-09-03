package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialTop;

public interface ImagensEventoPresencialTopRepository extends CrudRepository<ImagensEventoPresencialTop, Long> {
	
	@Query("select i from ImagensEventoPresencialTop i where i.id = :pid")
	ImagensEventoPresencialTop buscarPor(@Param("pid") Long id);

}
