package com.gilmarcarlos.developer.gcursos.service.usuarios;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.auth.Autorizacao;
import com.gilmarcarlos.developer.gcursos.model.usuarios.Usuario;
import com.gilmarcarlos.developer.gcursos.model.usuarios.UsuarioDTO;
import com.gilmarcarlos.developer.gcursos.model.usuarios.exceptions.UsuarioExisteException;
import com.gilmarcarlos.developer.gcursos.repository.auth.AutorizacaoRepository;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.UsuarioRepository;
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
	public Usuario salvar(Usuario usuario) throws UsuarioExisteException {
		if(emailExiste(usuario.getEmail(), usuario.getId())) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		return repository.save(usuario);
	}
	
	public Usuario criarNovo(Usuario usuario) throws UsuarioExisteException {
		
		if(emailExiste(usuario.getEmail())) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		
		usuario.setHabilitado(true);
		usuario.setAutorizacoes(Arrays.asList(autorizacaoRespository.findByNome("ROLE_Usuario")));
		usuario.setSenha(passwordCrypt.encode("zeus_1234@5"));
		return repository.save(usuario);
	}
	
	public Usuario atualizarDados(Usuario usuario) throws UsuarioExisteException {
		
		if(emailExiste(usuario.getEmail(), usuario.getId())) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		
		usuario.setHabilitado(true);
		usuario.setAutorizacoes(buscarPor(usuario.getId()).getAutorizacoes());
		usuario.setSenha(passwordCrypt.encode(buscarPor(usuario.getId()).getSenha()));
		return repository.save(usuario);
	}
	
	public Usuario atualizarDadosNoEncryptSenha(Usuario usuario) throws UsuarioExisteException {
		
		if(emailExiste(usuario.getEmail(), usuario.getId())) {
			throw new UsuarioExisteException("já existe um usuário cadastrado com esse email");
		}
		
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
	
	public Boolean emailExiste(String email, Long id) {
		return repository.existsByEmail(email, id);
	}
	
	public Boolean emailExiste(String email) {
		return repository.existsByEmail(email);
	}

	public Page<Usuario> listarTodos(Pageable pageable) {
		return repository.listarTodos(pageable);
	}
	
	public List<UsuarioDTO> listarUsuariosDTO(){
		
		List<UsuarioDTO> usuarios = new ArrayList<>();
		listarCadastrosCompleots().forEach( u ->usuarios.add(new UsuarioDTO(u)));		
		return usuarios;
	}

	public Page<Usuario> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}

	public Page<Usuario> buscarPorUnidade(Long id, Pageable pageable) {
		return repository.buscarPorUnidade(id, pageable);
	}
	
	public Page<Usuario> buscarPorCargo(Long id, Pageable pageable) {
		return repository.buscarPorCargo(id, pageable);
	}
}
