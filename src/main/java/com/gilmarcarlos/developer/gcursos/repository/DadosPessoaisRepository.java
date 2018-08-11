package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;

public interface DadosPessoaisRepository extends CrudRepository<DadosPessoais, Long> {
	
	@Query("select u from DadosPessoais u")
	List<DadosPessoais> listAll();
	
	@Query("select u from DadosPessoais u where u.cpf = :pcpf")
	DadosPessoais buscarPor(@Param("pcpf") String cpf);

}
