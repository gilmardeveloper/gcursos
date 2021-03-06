package com.gilmarcarlos.developer.gcursos.repository.locais;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.locais.Cargo;

/**
 * Interface para crud da entidade (Cargo)
 * 
 * @author Gilmar Carlos
 *
 */
public interface CargoRepository extends CrudRepository<Cargo, Long> {
	
	@Query("select c from Cargo c")
	List<Cargo> listAll();
	
	@Query("select c from Cargo c where c.id = :pid")
	Cargo buscarPor(@Param("pid") Long id);

}
