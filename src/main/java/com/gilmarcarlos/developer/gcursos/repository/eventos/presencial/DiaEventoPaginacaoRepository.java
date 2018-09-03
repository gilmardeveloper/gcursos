package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;

public interface DiaEventoPaginacaoRepository extends PagingAndSortingRepository<DiaEvento, Long> {
	
	
	@Query("select d from DiaEvento d order by d.data")
	Page<DiaEvento> listarTodos(Pageable pageable);
	
	@Query("select d from DiaEvento d where d.programacaoPresencial.id = :pid order by d.data")
	Page<DiaEvento> listarDiasPorProgramacao(@Param("pid")Long id, Pageable pageable);
	
	
}
