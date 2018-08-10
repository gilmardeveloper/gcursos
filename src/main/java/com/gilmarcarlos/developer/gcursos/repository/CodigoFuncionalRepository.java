package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.CodigoFuncional;

public interface CodigoFuncionalRepository extends CrudRepository<CodigoFuncional, Long> {
	
	@Query("select c from CodigoFuncional c")
	List<CodigoFuncional> listAll();

}
