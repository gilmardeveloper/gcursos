package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.repository.UnidadeTrabalhoRepository;

@Service
public class UnidadeTrabalhoService {

	@Autowired
	private UnidadeTrabalhoRepository repository;
	
	public UnidadeTrabalho salvar(UnidadeTrabalho unidadeTrabalho) {
		return repository.save(unidadeTrabalho);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<UnidadeTrabalho> listarTodos(){
		return repository.listAll();
	}

	public UnidadeTrabalho buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
