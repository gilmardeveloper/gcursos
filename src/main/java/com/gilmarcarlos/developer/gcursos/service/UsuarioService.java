package com.gilmarcarlos.developer.gcursos.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
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
	
	//@CacheEvict(value="postCache", allEntries=true)
	public Usuario salvar(Usuario usuario) {
		return repository.save(usuario);
	}
	
	public Usuario criarNovo(Usuario usuario) {
		usuario.setHabilitado(true);
		usuario.setAutorizacoes(Arrays.asList(autorizacaoRespository.findByNome("ROLE_Usuario")));
		usuario.setSenha(passwordCrypt.encode("zeus_1234@5"));
		return repository.save(usuario);
	}
	
	public Usuario atualizarDados(Usuario usuario) {
		usuario.setHabilitado(true);
		usuario.setAutorizacoes(buscarPor(usuario.getId()).getAutorizacoes());
		usuario.setSenha(passwordCrypt.encode(buscarPor(usuario.getId()).getSenha()));
		return repository.save(usuario);
	}
	
	public Usuario atualizarDadosNoEncryptSenha(Usuario usuario) {
		usuario.setHabilitado(true);
		usuario.setAutorizacoes(buscarPor(usuario.getId()).getAutorizacoes());
		usuario.setSenha(buscarPor(usuario.getId()).getSenha());
		return repository.save(usuario);
	}
	
	//@CacheEvict(value="postCache", allEntries=true)
	public Usuario atualizarNome(Usuario usuario) {
		
		Usuario temp = repository.findOne(usuario.getId());
		temp.setNome(usuario.getNome());
		return repository.save(temp);
	}
	
	//@CacheEvict(value="postCache", allEntries=true)
	public Usuario redefinirSenha(Usuario usuario) {
		
		Usuario temp = repository.findOne(usuario.getId());
		//usuario.setAutorizacoes(temp.getAutorizacoes());
		//usuario.setHabilitado(temp.isHabilitado());
		temp.setSenha(passwordCrypt.encode(usuario.getSenha()));
		
		return repository.save(temp);
	}
	
	//@CacheEvict(value="postCache", allEntries=true)
	public Usuario redefinirSenha(Usuario usuario, String senha) {
		usuario.setSenha(passwordCrypt.encode(senha));
		return repository.save(usuario);
	}
	
	//@CacheEvict(value="postCache", allEntries=true)
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public Usuario buscarPor(Long id) {
		return repository.findOne(id);
	}
	
	public Usuario buscarPor(String email) {
		return repository.findByEmail(email);
	}
	
	//@Cacheable("postCache")
	public List<Usuario> listarTodos(){
		return repository.listAll();
	}
	
	//@Cacheable("postCache")
	public List<Usuario> listarCadastrosCompleots(){
		return repository.listCadastrosCompleto();
	}

	public void atualizarDados(Usuario usuario, String autorizacaoNome) {
		
		Usuario temp = repository.findOne(usuario.getId());
		usuario.setSenha(temp.getSenha());
		usuario.setAutorizacoes(Arrays.asList(autorizacaoRespository.findByNome(autorizacaoNome)));
		usuario.setHabilitado(temp.isHabilitado());
		
		repository.save(usuario);
		
	}

	public Usuario atualizarAutorizacoes(Usuario usuario, String nomeAutorizacao) {
		List<Autorizacao> autorizacoes = new ArrayList<>();
		autorizacoes.add(autorizacaoRespository.findByNome(nomeAutorizacao));
		usuario.setAutorizacoes(autorizacoes);
		return repository.save(usuario);
	}
	
	
}
