package com.gilmarcarlos.developer.gcursos.repository.locais;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gilmarcarlos.developer.gcursos.model.locais.EnderecoUnidade;

public interface EnderecoUnidadeRepository extends CrudRepository<EnderecoUnidade, Long> {
	
	@Query("select e from EnderecoUnidade e")
	List<EnderecoUnidade> listAll();
	
	@Transactional
	@Modifying
	@Query("delete from EnderecoUnidade e where e.unidadeTrabalho.id = :pid")
	void deleteByUnidade(@Param("pid") Long id);
}
