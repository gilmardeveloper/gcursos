package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencialLog;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.LogEvePresencialPaginacaoRepository;

@Service
public class LogEvePresencialPaginacaoService {
	
	private final static Integer MINIMO_PAGES = 0;
	private final static Integer MAXIMO_PAGES = 50;

	@Autowired
	private LogEvePresencialPaginacaoRepository repository;
	
	public Page<EventoPresencialLog> listarTodos(Pageable pageable){
		return repository.listarTodos(pageable);
	}
	
	public Page<EventoPresencialLog> listarLogsPorEvento(Long id, Pageable pageable){
		return repository.listarLogsPorEvento(id, pageable);
	}

	
}
