package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUsuario;

public interface TelefoneUsuarioRepository extends CrudRepository<TelefoneUsuario , Long> {
	
	@Query("select t from TelefoneUsuario t")
	List<TelefoneUsuario> listAll();

}
