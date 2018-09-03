package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.usuarios.TelefoneUsuario;

public interface TelefoneUsuarioRepository extends CrudRepository<TelefoneUsuario , Long> {
	
	@Query("select t from TelefoneUsuario t")
	List<TelefoneUsuario> listAll();
	
	@Query("select t from TelefoneUsuario t where t.numero = :pnumero")
	TelefoneUsuario buscarPor(@Param("pnumero") String numero);

	@Query("select t from TelefoneUsuario t where t.id = :pid")
	TelefoneUsuario buscarPor(@Param("pid") Long id);

}
