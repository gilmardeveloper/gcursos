package com.gilmarcarlos.developer.gcursos.repository.usuarios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByEmail(String email);

	@Query("select u from Usuario u")
	List<Usuario> listAll();
	
	@Query("select u from Usuario u join fetch u.dadosPessoais d where d.usuario.id = u.id")
	List<Usuario> listCadastrosCompleto();
	
	@Query("select u from Usuario u where u.id = :pid")
	Usuario findOne(@Param("pid") Long id);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.email = :pemail")
    boolean existsByEmail(@Param("pemail") String email);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.email = :pemail and u.id != :pid")
	boolean existsByEmail(@Param("pemail") String email, @Param("pid") Long id);
	
	@Query("select u from Usuario u")
	Page<Usuario> listarTodos(Pageable pageable);
	
	@Query("select u from Usuario u where u.id = :pid")
	Page<Usuario> buscarPor(@Param("pid") Long id, Pageable pageable);
	
	@Query("select u from Usuario u where u.codigoFuncional.unidadeTrabalho.departamento.id = :pid")
	Page<Usuario> buscarPorDepartamento(@Param("pid") Long id, Pageable pageable);
	
	@Query("select u from Usuario u where u.codigoFuncional.unidadeTrabalho.id = :pid")
	Page<Usuario> buscarPorUnidade(@Param("pid") Long id, Pageable pageable);
	
	@Query("select u from Usuario u where u.codigoFuncional.cargo.id = :pid")
	Page<Usuario> buscarPorCargo(@Param("pid") Long id, Pageable pageable);

	
}
