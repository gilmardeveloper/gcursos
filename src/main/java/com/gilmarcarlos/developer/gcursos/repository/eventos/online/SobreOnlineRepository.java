package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.SobreOnline;

/**
 * Interface para crud da entidade (SobreOnline)
 * 
 * @author Gilmar Carlos
 *
 */
public interface SobreOnlineRepository extends CrudRepository<SobreOnline, Long> {
	
	@Query("select c from SobreOnline c")
	List<SobreOnline> listAll();
	
	@Query("select c from SobreOnline c where c.id = :pid")
	SobreOnline buscarPor(@Param("pid") Long id);

}
