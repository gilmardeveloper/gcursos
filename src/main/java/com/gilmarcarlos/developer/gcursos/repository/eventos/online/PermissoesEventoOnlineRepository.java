package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.PermissoesEventoOnline;

/**
 * Interface para crud da entidade (PermissoesEventoOnline)
 * 
 * @author Gilmar Carlos
 *
 */
public interface PermissoesEventoOnlineRepository extends CrudRepository<PermissoesEventoOnline, Long> {
	
	@Query("select p from PermissoesEventoOnline p")
	List<PermissoesEventoOnline> listAll();
	
	@Query("select p from PermissoesEventoOnline p where p.id = :pid")
	PermissoesEventoOnline buscarPor(@Param("pid") Long id);

}
