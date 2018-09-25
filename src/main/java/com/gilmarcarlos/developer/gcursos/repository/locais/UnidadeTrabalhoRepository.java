package com.gilmarcarlos.developer.gcursos.repository.locais;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;

/**
 * Interface para crud da entidade (UnidadeTrabalho)
 * 
 * @author Gilmar Carlos
 *
 */
public interface UnidadeTrabalhoRepository extends CrudRepository<UnidadeTrabalho, Long> {
	
	@Query(value = "select distinct u from UnidadeTrabalho u join fetch u.telefones")
	List<UnidadeTrabalho> listAll();
	
	@Query(value = "select u from UnidadeTrabalho u")
	List<UnidadeTrabalho> listaTodos();
	
	@Query(value = "select distinct u from UnidadeTrabalho u join fetch u.telefones where u.departamento.id = :pdepartamento")
	List<UnidadeTrabalho> listAll(@Param("pdepartamento") Long departamento);
	
	@Query("select u from UnidadeTrabalho u where u.id = :pid")
	UnidadeTrabalho buscarPor(@Param("pid")Long id);


}
