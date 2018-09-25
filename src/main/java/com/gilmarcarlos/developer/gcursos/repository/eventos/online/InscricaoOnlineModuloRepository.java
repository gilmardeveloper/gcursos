package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnlineModulo;

/**
 * Interface para crud da entidade (InscricaoOnlineModulo)
 * 
 * @author Gilmar Carlos
 *
 */
public interface InscricaoOnlineModuloRepository extends CrudRepository<InscricaoOnlineModulo, Long> {
	
	@Query("select i from InscricaoOnlineModulo i")
	List<InscricaoOnlineModulo> listAll();
	
	@Query("select i from InscricaoOnlineModulo i where i.id = :pid")
	InscricaoOnlineModulo buscarPor(@Param("pid") Long id);
	
}
