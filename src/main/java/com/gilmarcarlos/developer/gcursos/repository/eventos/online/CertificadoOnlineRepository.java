package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.CertificadoOnline;

/**
 * Interface para crud da entidade (CertificadoEventoOnline)
 * 
 * @author Gilmar Carlos
 *
 */
public interface CertificadoOnlineRepository extends CrudRepository<CertificadoOnline, Long> {
	
	@Query("select c from CertificadoOnline c where c.eventoOnline.id = :pid")
	CertificadoOnline buscarPor(@Param("pid") Long id);

}
