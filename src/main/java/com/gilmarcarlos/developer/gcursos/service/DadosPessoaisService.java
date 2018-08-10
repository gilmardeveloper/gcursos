package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.DadosPessoais;
import com.gilmarcarlos.developer.gcursos.repository.DadosPessoaisRepository;

@Service
public class DadosPessoaisService {

	@Autowired
	private DadosPessoaisRepository repository;
	
	public DadosPessoais salvar(DadosPessoais cargo) {
		return repository.save(cargo);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<DadosPessoais> listarTodos(){
		return repository.listAll();
	}
	
	
}
