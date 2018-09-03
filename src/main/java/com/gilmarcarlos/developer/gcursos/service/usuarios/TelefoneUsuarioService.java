package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.TelefoneUsuario;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.TelefoneUsuarioRepository;

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

	public TelefoneUsuario buscarPor(String numero) {
		return repository.buscarPor(numero);
	}

	public TelefoneUsuario buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
