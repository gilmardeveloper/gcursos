package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;

/**
 * Interface para crud da entidade (Modulo)
 * 
 * @author Gilmar Carlos
 *
 */
public interface ModuloRepository extends CrudRepository<Modulo, Long> {
	
	@Query("select m from Modulo m")
	List<Modulo> listAll();
	
	@Query("select m from Modulo m where m.id = :pid")
	Modulo buscarPor(@Param("pid")Long id);

	@Query("select m from Modulo m")
	Page<Modulo> listarTodos(Pageable pageable);

}
