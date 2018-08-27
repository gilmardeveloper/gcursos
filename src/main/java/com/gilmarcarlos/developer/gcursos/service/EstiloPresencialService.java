package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EstiloPresencial;
import com.gilmarcarlos.developer.gcursos.repository.EstiloPresencialRepository;

@Service
public class EstiloPresencialService {

	@Autowired
	private EstiloPresencialRepository repository;
	
	public EstiloPresencial salvar(EstiloPresencial estilo) {
		return repository.save(estilo);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<EstiloPresencial> listarTodos(){
		return repository.listAll();
	}

	public EstiloPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
