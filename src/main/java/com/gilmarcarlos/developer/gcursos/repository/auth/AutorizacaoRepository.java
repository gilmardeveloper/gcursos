package com.gilmarcarlos.developer.gcursos.repository.auth;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;

/**
 * Interface para crud da entidade (Autorizacao)
 * 
 * @author Gilmar Carlos
 *
 */
public interface AutorizacaoRepository extends CrudRepository<Autorizacao, Long>{

	Autorizacao findByNome(String nome);
	
	@Query("select a from Autorizacao a")
	List<Autorizacao> listAll();

}
