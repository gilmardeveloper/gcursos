package com.gilmarcarlos.developer.gcursos.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensLogoListaPresenca;

public interface ImagensLogoListaPresencaRepository extends CrudRepository<ImagensLogoListaPresenca, Long> {
	
	@Query("select i from ImagensLogoListaPresenca i where i.id = :pid")
	ImagensLogoListaPresenca buscarPor(@Param("pid") Long id);

}
