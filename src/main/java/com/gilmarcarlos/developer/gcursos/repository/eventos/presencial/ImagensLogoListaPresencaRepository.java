package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.ImagensLogoListaPresenca;

/**
 * Interface para crud da entidade (ImagensLogoListaPresenca)
 * 
 * @author Gilmar Carlos
 *
 */
public interface ImagensLogoListaPresencaRepository extends CrudRepository<ImagensLogoListaPresenca, Long> {
	
	@Query("select i from ImagensLogoListaPresenca i where i.id = :pid")
	ImagensLogoListaPresenca buscarPor(@Param("pid") Long id);

}
