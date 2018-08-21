package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.DiaEvento;
import com.gilmarcarlos.developer.gcursos.repository.DiaEventoRepository;

@Service
public class DiaEventoService {

	@Autowired
	private DiaEventoRepository repository;
	
	public DiaEvento salvar(DiaEvento diaEvento) {
		return repository.save(diaEvento);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<DiaEvento> listarTodos(){
		return repository.listAll();
	}

	public DiaEvento buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
