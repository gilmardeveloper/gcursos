package com.gilmarcarlos.developer.gcursos.repository.notifcacoes;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;

/**
 * Interface para crud da entidade (Notificacao)
 * 
 * @author Gilmar Carlos
 *
 */
public interface NotificacaoRepository extends CrudRepository<Notificacao, Long> {
	
	@Query("select n from Notificacao n")
	List<Notificacao> listAll();
	
	@Query("select n from Notificacao n where n.id = :pid")
	Notificacao buscarPor(@Param("pid") Long id);

}
