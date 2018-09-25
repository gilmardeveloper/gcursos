package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnlineAtividade;

/**
 * Interface para crud da entidade (InscricaoOnlineAtividade)
 * 
 * @author Gilmar Carlos
 *
 */
public interface InscricaoOnlineAtividadeRepository extends CrudRepository<InscricaoOnlineAtividade, Long> {
	
	@Query("select i from InscricaoOnlineAtividade i")
	List<InscricaoOnlineAtividade> listAll();
	
	@Query("select i from InscricaoOnlineAtividade i where i.id = :pid")
	InscricaoOnlineAtividade buscarPor(@Param("pid") Long id);
	
}
