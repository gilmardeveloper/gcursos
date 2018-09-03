package com.gilmarcarlos.developer.gcursos.repository.locais;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.locais.Departamento;

public interface DepartamentoRepository extends CrudRepository<Departamento, Long> {
	
	@Query("select d from Departamento d")
	List<Departamento> listAll();
	
	@Query("select d from Departamento d where d.id = :pid")
	Departamento buscaPorId(@Param("pid") Long id);

}
