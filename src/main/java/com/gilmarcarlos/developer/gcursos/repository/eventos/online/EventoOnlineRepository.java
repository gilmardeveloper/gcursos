package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;


/**
 * Interface para crud da entidade (EventoOnline)
 * 
 * @author Gilmar Carlos
 *
 */
public interface EventoOnlineRepository extends CrudRepository<EventoOnline, Long> {
	
	@Query("select e from EventoOnline e")
	List<EventoOnline> listAll();
	
	
	@Query("select e from EventoOnline e where e.id = :pid")
	EventoOnline buscarPor(@Param("pid")Long id);

	List<EventoOnline> findByPublicadoTrue();
	
	Page<EventoOnline> findByPublicadoTrue(Pageable pageable);

	@Query("select e from EventoOnline e")
	Page<EventoOnline> listarTodos(Pageable pageable);

	@Query("select e from EventoOnline e where e.responsavel.id = :pid")
	Page<EventoOnline> listarTodos(@Param("pid") Long id, Pageable pageable);

	
	@Query("select distinct e from EventoOnline e join fetch e.inscricoes i where i.usuario.id = :pid")
	List<EventoOnline> buscarPorUsuario(@Param("pid") Long id);

	@Query("select e from EventoOnline e where e.id = :pid")
	Page<EventoOnline> buscarPor(@Param("pid") Long id, Pageable pageable);

	@Query("select distinct e from EventoOnline e join e.inscricoes i where i.usuario.id = :pid")
	Page<EventoOnline> buscarPorUsuario(@Param("pid") Long id, Pageable pageable);


}
