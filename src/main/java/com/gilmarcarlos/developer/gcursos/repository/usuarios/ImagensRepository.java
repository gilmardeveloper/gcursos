package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.images.Imagens;

/**
 * Interface para crud da entidade (Imagens)
 * 
 * @author Gilmar Carlos
 *
 */
public interface ImagensRepository extends CrudRepository<Imagens, Long> {
	
	@Query("select i from Imagens i where i.id = :pid")
	Imagens buscarPor(@Param("pid") Long id);

}
