package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;

public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {
	
	@Query("select n from Notificacao n")
	List<Notificacao> listAll();
	
	@Query("select n from Notificacao n where n.id = :pid")
	Notificacao buscarPor(@Param("pid") Long id);

}
