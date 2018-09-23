package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;

public interface EventoPresencialRepository extends CrudRepository<EventoPresencial, Long> {
	
	@Query("select e from EventoPresencial e")
	List<EventoPresencial> listAll();
	
	@Query("select distinct e from EventoPresencial e join fetch e.inscricoes i where i.usuario.id = :pid")
	List<EventoPresencial> buscarPorUsuario(@Param("pid")Long id);
	
	@Query("select e from EventoPresencial e where e.id = :pid")
	EventoPresencial buscarPor(@Param("pid")Long id);

	List<EventoPresencial> findByPublicadoTrue();
	
	Page<EventoPresencial> findByPublicadoTrue(Pageable pageable);

	@Query("select e from EventoPresencial e")
	Page<EventoPresencial> listarTodos(Pageable pageable);
	
	@Query("select e from EventoPresencial e where e.id = :pid")
	Page<EventoPresencial> buscarPor(@Param("pid") Long id, Pageable pageable);
	
	@Query("select e from EventoPresencial e where e.dataInicio between :pinicio and :ptermino")
	Page<EventoPresencial> buscarPor(@Param("pinicio") LocalDate inicio, @Param("ptermino") LocalDate termino, Pageable pageable);

	@Query("select e from EventoPresencial e where e.dataInicio between :pinicio and :ptermino and e.responsavel.id = :pid")
	Page<EventoPresencial> buscarPor(@Param("pid") Long id, @Param("pinicio") LocalDate inicio, @Param("ptermino") LocalDate termino, Pageable pageable);
	
	@Query("select distinct e from EventoPresencial e join e.inscricoes i where i.usuario.id = :pid")
	Page<EventoPresencial> buscarPorUsuario(@Param("pid") Long id, Pageable pageable);

	@Query("select e from EventoPresencial e where e.responsavel.id = :pid")
	Page<EventoPresencial> buscarPorResponsavel(@Param("pid") Long id, Pageable pageable);

}
