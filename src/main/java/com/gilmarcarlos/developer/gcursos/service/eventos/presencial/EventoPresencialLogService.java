package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencialLog;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.EventoPresencialLogRepository;

@Service
public class EventoPresencialLogService {

	@Autowired
	private EventoPresencialLogRepository repository;
	
	public EventoPresencialLog salvar(EventoPresencialLog log) {
		return repository.save(log);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<EventoPresencialLog> listarTodos(){
		return repository.listAll();
	}

	public EventoPresencialLog buscarPor(Long id) {
		return repository.buscarPor(id);
	}
		
}
