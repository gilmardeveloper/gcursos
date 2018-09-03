package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.usuarios.NomeColaborador;

public interface NomeColaboradorRepository extends CrudRepository<NomeColaborador, Long> {
	
	@Query("select c from NomeColaborador c")
	List<NomeColaborador> listAll();
	
	@Query("select c from NomeColaborador c where c.id = :pid")
	NomeColaborador buscarPor(@Param("pid") Long id);

}
