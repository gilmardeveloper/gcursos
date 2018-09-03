package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineDestaque;

public interface ImagensEventoOnlineDestaqueRepository extends CrudRepository<ImagensEventoOnlineDestaque, Long> {
	
	@Query("select i from ImagensEventoOnlineDestaque i where i.id = :pid")
	ImagensEventoOnlineDestaque buscarPor(@Param("pid") Long id);

}
