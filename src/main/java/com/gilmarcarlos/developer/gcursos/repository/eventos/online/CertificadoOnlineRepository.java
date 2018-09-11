package com.gilmarcarlos.developer.gcursos.repository.eventos.online;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.CertificadoOnline;

public interface CertificadoOnlineRepository extends CrudRepository<CertificadoOnline, Long> {
	
	@Query("select i from CertificadoOnline i where i.id = :pid")
	CertificadoOnline buscarPor(@Param("pid") Long id);

}
