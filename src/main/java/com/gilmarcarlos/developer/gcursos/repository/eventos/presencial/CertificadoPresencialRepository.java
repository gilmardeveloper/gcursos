package com.gilmarcarlos.developer.gcursos.repository.eventos.presencial;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.CertificadoPresencial;

/**
 * Interface para crud da entidade (CertificadoPresencial)
 * 
 * @author Gilmar Carlos
 *
 */
public interface CertificadoPresencialRepository extends CrudRepository<CertificadoPresencial, Long> {
	
	@Query("select c from CertificadoPresencial c where c.eventoPresencial.id = :pid")
	CertificadoPresencial buscarPor(@Param("pid") Long id);

}
