package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.CertificadoPresencial;

public interface CertificadoPresencialRepository extends CrudRepository<CertificadoPresencial, Long> {
	
	@Query("select i from CertificadoPresencial i where i.id = :pid")
	CertificadoPresencial buscarPor(@Param("pid") Long id);

}
