package com.gilmarcarlos.developer.gcursos.repository.locais;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.locais.CodigoFuncional;

/**
 * Interface para crud da entidade (CodigoFuncional)
 * 
 * @author Gilmar Carlos
 *
 */
public interface CodigoFuncionalRepository extends CrudRepository<CodigoFuncional, Long> {
	
	@Query("select c from CodigoFuncional c")
	List<CodigoFuncional> listAll();
	
	@Query("select c from CodigoFuncional c where c.codigo = :pcodigoFuncional")
	CodigoFuncional buscarPor(@Param("pcodigoFuncional") String codigoFuncional);
	
	@Query("select c from CodigoFuncional c where c.id = :pid")
	CodigoFuncional buscarPor(@Param("pid") Long id);

}
