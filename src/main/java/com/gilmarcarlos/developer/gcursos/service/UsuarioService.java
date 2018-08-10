package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.repository.AutorizacaoRepository;
import com.gilmarcarlos.developer.gcursos.repository.UsuarioRepository;
import com.gilmarcarlos.developer.gcursos.security.crypt.PasswordCrypt;

@Service
public class UsuarioService {

	@Autowired
	private PasswordCrypt passwordCrypt;
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private AutorizacaoRepository autorizacaoRespository;
	
	@CacheEvict(value="postCache", allEntries=true)
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}
	
	@CacheEvict(value="postCache", allEntries=true)
	public Usuario redefinirSenha(Usuario usuario) {
		
		Usuario temp = repository.findOne(usuario.getId());
		usuario.setAutorizacoes(temp.getAutorizacoes());
		usuario.setHabilitado(temp.isHabilitado());
		usuario.setSenha(passwordCrypt.encode(usuario.getSenha()));
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
