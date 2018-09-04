package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnlineLog;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.EventoOnlineLogRepository;

@Service
public class EventoOnlineLogService {

	@Autowired
	private EventoOnlineLogRepository repository;
	
	public EventoOnlineLog salvar(EventoOnlineLog log) {
		return repository.save(log);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<EventoOnlineLog> listarTodos(){
		return repository.listAll();
	}

	public EventoOnlineLog buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public Page<EventoOnlineLog> listarTodos(Pageable pageable){
		return repository.listarTodos(pageable);
	}
	
	public Page<EventoOnlineLog> listarLogsPorEvento(Long id, Pageable pageable){
		return repository.listarLogsPorEvento(id, pageable);
	}
		
}
