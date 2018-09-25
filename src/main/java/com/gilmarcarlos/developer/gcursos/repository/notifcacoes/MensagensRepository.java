package com.gilmarcarlos.developer.gcursos.repository.notifcacoes;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.notifications.Mensagens;

/**
 * Interface para crud da entidade (Mensagens)
 * 
 * @author Gilmar Carlos
 *
 */
public interface MensagensRepository extends CrudRepository<Mensagens, Long> {
	
	@Query("select n from Mensagens n")
	List<Mensagens> listAll();
	
	@Query("select n from Mensagens n where n.id = :pid")
	Mensagens buscarPor(@Param("pid") Long id);

}
