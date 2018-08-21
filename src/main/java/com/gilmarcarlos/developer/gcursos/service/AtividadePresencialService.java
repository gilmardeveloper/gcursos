package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.repository.AtividadePresencialRepository;

@Service
public class AtividadePresencialService {

	@Autowired
	private AtividadePresencialRepository repository;
	
	public AtividadePresencial salvar(AtividadePresencial atividade) {
		return repository.save(atividade);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<AtividadePresencial> listarTodos(){
		return repository.listAll();
	}

	public AtividadePresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public List<AtividadePresencial> buscarPorEvento(Long id) {
		return repository.buscarPorEvento(id);
	}
	
	
}
