package com.gilmarcarlos.developer.gcursos.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.auth.Permissoes;

public interface PermissoesRepository extends CrudRepository<Permissoes, Long> {

	@Query("select p from Permissoes p")
	List<Permissoes> listarTodos();
	
	@Query("select p from Permissoes p where p.id = :pid")
	Permissoes buscarPor(@Param("pid") Long id);

}
