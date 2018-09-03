package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gilmarcarlos.developer.gcursos.model.usuarios.EnderecoUsuario;

public interface EnderecoUsuarioRepository extends CrudRepository<EnderecoUsuario, Long> {
	
	@Query("select e from EnderecoUsuario e")
	List<EnderecoUsuario> listAll();

}
