package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.type.Escolaridade;

/**
 * Interface para crud da entidade (Escolaridade)
 * 
 * @author Gilmar Carlos
 *
 */
public interface EscolaridadeRepository extends CrudRepository<Escolaridade, Long> {
	
	@Query("select c from Escolaridade c")
	List<Escolaridade> listAll();
	
	@Query("select c from Escolaridade c where c.id = :pid")
	Escolaridade buscarPor(@Param("pid") Long id);

}
