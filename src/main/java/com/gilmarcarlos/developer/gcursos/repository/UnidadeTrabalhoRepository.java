package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.UnidadeTrabalho;

public interface UnidadeTrabalhoRepository extends CrudRepository<UnidadeTrabalho, Long> {
	
	@Query("select u from UnidadeTrabalho u join fetch u.telefones")
	List<UnidadeTrabalho> listAll();
	
	@Query("select u from UnidadeTrabalho u where u.id = :pid")
	UnidadeTrabalho buscarPor(@Param("pid")Long id);

}
