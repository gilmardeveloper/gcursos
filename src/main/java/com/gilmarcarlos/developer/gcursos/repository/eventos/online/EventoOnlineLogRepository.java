package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnlineLog;

public interface EventoOnlineLogRepository extends CrudRepository<EventoOnlineLog, Long> {
	
	@Query("select e from EventoOnlineLog e order by e.data desc")
	List<EventoOnlineLog> listAll();
		
	@Query("select e from EventoPresencialLog e where e.id = :pid")
	EventoOnlineLog buscarPor(@Param("pid") Long id);
	
	@Query("select e from EventoOnlineLog e order by e.data desc")
	Page<EventoOnlineLog> listarTodos(Pageable pageable);

	@Query("select e from EventoOnlineLog e where e.eventoOnline.id = :pid order by e.data desc")
	Page<EventoOnlineLog> listarLogsPorEvento(@Param("pid")Long id, Pageable pageable);
	
}
