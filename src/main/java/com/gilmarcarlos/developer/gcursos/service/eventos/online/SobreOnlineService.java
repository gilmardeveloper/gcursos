package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.SobreOnline;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.SobreOnlineRepository;

@Service
public class SobreOnlineService {

	@Autowired
	private SobreOnlineRepository repository;
	
	public SobreOnline salvar(SobreOnline sobre) {
		return repository.save(sobre);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<SobreOnline> listarTodos(){
		return repository.listAll();
	}

	public SobreOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
