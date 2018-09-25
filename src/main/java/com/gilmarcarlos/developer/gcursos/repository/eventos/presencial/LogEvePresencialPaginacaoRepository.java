package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencialLog;

/**
 * Interface para crud da entidade (EventoPresencialLog)
 * 
 * @author Gilmar Carlos
 *
 */
public interface LogEvePresencialPaginacaoRepository extends PagingAndSortingRepository<EventoPresencialLog, Long> {
	
	
	@Query("select e from EventoPresencialLog e order by e.data desc")
	Page<EventoPresencialLog> listarTodos(Pageable pageable);
	
	@Query("select e from EventoPresencialLog e where e.eventoPresencial.id = :pid order by e.data desc")
	Page<EventoPresencialLog> listarLogsPorEvento(@Param("pid")Long id, Pageable pageable);
	
	
}
