package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EstiloOnline;

/**
 * Interface para crud da entidade (EstiloOnline)
 * 
 * @author Gilmar Carlos
 *
 */
public interface EstiloOnlineRepository extends CrudRepository<EstiloOnline, Long> {
	
	@Query("select e from EstiloOnline e")
	List<EstiloOnline> listAll();
	
	@Query("select e from EstiloOnline e where e.id = :pid")
	EstiloOnline buscarPor(@Param("pid") Long id);

}
