package com.gilmarcarlos.developer.gcursos.repository.locais;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gilmarcarlos.developer.gcursos.model.locais.TelefoneUnidade;

/**
 * Interface para crud da entidade (TelefoneUnidade)
 * 
 * @author Gilmar Carlos
 *
 */
public interface TelefoneUnidadeRepository extends CrudRepository<TelefoneUnidade, Long> {
	
	@Query("select t from TelefoneUnidade t")
	List<TelefoneUnidade> listAll();

	@Transactional
	@Modifying
	@Query("delete from TelefoneUnidade t where t.unidadeTrabalho.id = :pid")
	void deleteByUnidade(@Param("pid") Long id);

}
