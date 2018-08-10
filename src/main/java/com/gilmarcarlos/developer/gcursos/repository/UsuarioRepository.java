package com.gilmarcarlos.developer.gcursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByEmail(String email);

	@Query("select u from Usuario u")
	List<Usuario> listAll();
	
	@Query("select u from Usuario u where u.id = :pid")
	Usuario findOne(@Param("pid") Long id);

}
