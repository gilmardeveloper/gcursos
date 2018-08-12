package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;

public interface DadosPessoaisRepository extends CrudRepository<DadosPessoais, Long> {
	
	@Query("select d from DadosPessoais d join fetch d.telefones")
	List<DadosPessoais> listAll();
	
	@Query("select d from DadosPessoais d join fetch d.telefones where d.cpf = :pcpf")
	DadosPessoais buscarPor(@Param("pcpf") String cpf);

}
