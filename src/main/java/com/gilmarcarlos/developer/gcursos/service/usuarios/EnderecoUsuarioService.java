package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.EnderecoUsuario;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.EnderecoUsuarioRepository;

@Service
public class EnderecoUsuarioService {

	@Autowired
	private EnderecoUsuarioRepository repository;
	

	public EnderecoUsuario salvar(EnderecoUsuario enderecoUsuario) {
		return repository.save(enderecoUsuario);
	}
	

	public void deletar(Long id) {
		repository.deleteById(id);
	}
	

	public List<EnderecoUsuario> listarTodos(){
		return repository.listAll();
	}
	
	
}
