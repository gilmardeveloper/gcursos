package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.type.Escolaridade;
import com.gilmarcarlos.developer.gcursos.repository.EscolaridadeRepository;

@Service
public class EscolaridadeService {

	@Autowired
	private EscolaridadeRepository repository;
	
	public Escolaridade salvar(Escolaridade escolaridade) {
		return repository.save(escolaridade);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Escolaridade> listarTodos(){
		return repository.listAll();
	}

	public Escolaridade buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
