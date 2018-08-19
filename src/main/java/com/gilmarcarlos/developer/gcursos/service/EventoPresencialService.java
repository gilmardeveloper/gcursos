package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.repository.EventoPresencialRepository;

@Service
public class EventoPresencialService {

	@Autowired
	private EventoPresencialRepository repository;
	
	
	public EventoPresencial salvar(EventoPresencial eventoPresencial) {
		return repository.save(eventoPresencial);
	}
	
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	

	public List<EventoPresencial> listarTodos(){
		return repository.listAll();
	}

	public EventoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
