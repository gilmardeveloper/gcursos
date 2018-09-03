package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.PermissoesEventoOnline;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.PermissoesEventoOnlineRepository;

@Service
public class PermissoesEventoOnlineService {

	@Autowired
	private PermissoesEventoOnlineRepository repository;
	
	public PermissoesEventoOnline salvar(PermissoesEventoOnline permissoes) {
		return repository.save(permissoes);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<PermissoesEventoOnline> listarTodos(){
		return repository.listAll();
	}

	public PermissoesEventoOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
}
