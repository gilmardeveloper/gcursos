package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.type.Sexo;

/**
 * Interface para crud da entidade (Sexo)
 * 
 * @author Gilmar Carlos
 *
 */
public interface SexoRepository extends CrudRepository<Sexo, Long> {
	
	@Query("select c from Sexo c")
	List<Sexo> listAll();
	
	@Query("select c from Sexo c where c.id = :pid")
	Sexo buscarPor(@Param("pid") Long id);

}
