package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.EstiloOnline;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.EstiloOnlineRepository;

@Service
public class EstiloOnlineService {

	@Autowired
	private EstiloOnlineRepository repository;
	
	public EstiloOnline salvar(EstiloOnline estilo) {
		return repository.save(estilo);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<EstiloOnline> listarTodos(){
		return repository.listAll();
	}

	public EstiloOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
