package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.DiaEvento;

public interface DiaEventoRepository extends CrudRepository<DiaEvento, Long> {
	
	@Query("select d from DiaEvento d join fetch d.atividades")
	List<DiaEvento> listAll();
	
	@Query("select d from DiaEvento d join fetch d.atividades where d.id = :pid")
	DiaEvento buscarPor(@Param("pid") Long id);
	
}
