package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.AtividadeOnline;

public interface AtividadeOnlineRepository extends CrudRepository<AtividadeOnline, Long> {
	
	@Query("select a from AtividadeOnline a")
	List<AtividadeOnline> listAll();
	
	@Query("select a from AtividadeOnline a where a.id = :pid")
	AtividadeOnline buscarPor(@Param("pid")Long id);

	@Query("select a from AtividadeOnline a")
	Page<AtividadeOnline> listarTodos(Pageable pageable);

}
