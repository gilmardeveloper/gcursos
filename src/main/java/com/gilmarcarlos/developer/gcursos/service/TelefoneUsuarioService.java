package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.repository.TelefoneUsuarioRepository;

@Service
public class TelefoneUsuarioService {

	@Autowired
	private TelefoneUsuarioRepository repository;
	
	public TelefoneUsuario salvar(TelefoneUsuario telefoneUsuario) {
		return repository.save(telefoneUsuario);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<TelefoneUsuario> listarTodos(){
		return repository.listAll();
	}
	
	
}
