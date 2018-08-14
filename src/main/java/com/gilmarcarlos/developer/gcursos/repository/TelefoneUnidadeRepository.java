package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUnidade;

public interface TelefoneUnidadeRepository extends CrudRepository<TelefoneUnidade, Long> {
	
	@Query("select t from TelefoneUnidade t")
	List<TelefoneUnidade> listAll();

	@Transactional
	@Modifying
	@Query("delete from TelefoneUnidade t where t.unidadeTrabalho.id = :pid")
	void deleteByUnidade(@Param("pid") Long id);

}
