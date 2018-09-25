package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.usuarios.DadosPessoais;

/**
 * Interface para crud da entidade (DadosPessoais)
 * 
 * @author Gilmar Carlos
 *
 */
public interface DadosPessoaisRepository extends CrudRepository<DadosPessoais, Long> {
	
	@Query("select d from DadosPessoais d join fetch d.telefones")
	List<DadosPessoais> listAll();
	
	@Query("select d from DadosPessoais d join fetch d.telefones where d.cpf = :pcpf")
	DadosPessoais buscarPor(@Param("pcpf") String cpf);
	
	@Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DadosPessoais d WHERE d.cpf = :pcpf")
    boolean existsByCpf(@Param("pcpf") String cpf);

	@Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DadosPessoais d WHERE d.cpf = :pcpf and d.usuario.id != :pid")
	boolean existsByCpf(@Param("pcpf") String cpf, @Param("pid") Long id);

}
