package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoPresencialDestaque;

public interface ImagensEventoPresencialDestaqueRepository extends CrudRepository<ImagensEventoPresencialDestaque, Long> {
	
	@Query("select i from ImagensEventoPresencialDestaque i where i.id = :pid")
	ImagensEventoPresencialDestaque buscarPor(@Param("pid") Long id);

}
