package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EstiloPresencial;

/**
 * Interface para crud da entidade (EstiloPresencial)
 * 
 * @author Gilmar Carlos
 *
 */
public interface EstiloPresencialRepository extends CrudRepository<EstiloPresencial, Long> {
	
	@Query("select e from EstiloPresencial e")
	List<EstiloPresencial> listAll();
	
	@Query("select e from EstiloPresencial e where e.id = :pid")
	EstiloPresencial buscarPor(@Param("pid") Long id);

}
