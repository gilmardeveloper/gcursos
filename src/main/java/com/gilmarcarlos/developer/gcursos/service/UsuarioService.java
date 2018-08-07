package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.UsuarioRepository;

@Service
public class UsuarioService {

	
	@Autowired
	private UsuarioRepository repository;
	
	@CacheEvict(value="postCache", allEntries=true)
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}
	
	@CacheEvict(value="postCache", allEntries=true)
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public Usuario buscarPor(Long id) {
		return repository.findOne(id);
	}
	
	public Usuario buscarPor(String email) {
		return repository.findByEmail(email);
	}
	
	@Cacheable("postCache")
	public List<Usuario> listarTodos(){
		return repository.listAll();
	}
	
	
}
