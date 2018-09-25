package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensEventoOnlineTop;

/**
 * Interface para crud da entidade (ImagensEventoOnlineTop)
 * 
 * @author Gilmar Carlos
 *
 */
public interface ImagensEventoOnlineTopRepository extends CrudRepository<ImagensEventoOnlineTop, Long> {
	
	@Query("select i from ImagensEventoOnlineTop i where i.id = :pid")
	ImagensEventoOnlineTop buscarPor(@Param("pid") Long id);

}
