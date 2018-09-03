package com.gilmarcarlos.developer.gcursos.repository.eventos.categorias;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;

public interface CategoriaEventoRepository extends CrudRepository<CategoriaEvento, Long> {
	
	@Query("select c from CategoriaEvento c")
	List<CategoriaEvento> listAll();
	
	@Query("select c from CategoriaEvento c where c.id = :pid")
	CategoriaEvento buscarPor(@Param("pid") Long id);

}
