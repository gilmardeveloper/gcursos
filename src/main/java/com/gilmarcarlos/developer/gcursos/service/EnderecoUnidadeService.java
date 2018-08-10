package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.EnderecoUnidade;
import com.gilmarcarlos.developer.gcursos.repository.EnderecoUnidadeRepository;

@Service
public class EnderecoUnidadeService {

	@Autowired
	private EnderecoUnidadeRepository repository;
	
	public EnderecoUnidade salvar(EnderecoUnidade enderecoUnidade) {
		return repository.save(enderecoUnidade);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<EnderecoUnidade> listarTodos(){
		return repository.listAll();
	}
	
	
}
