package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.PermissoesEventoPresencial;
import com.gilmarcarlos.developer.gcursos.repository.PermissoesEventoPresencialRepository;

@Service
public class PermissoesEventoPresencialService {

	@Autowired
	private PermissoesEventoPresencialRepository repository;
	
	public PermissoesEventoPresencial salvar(PermissoesEventoPresencial permissoes) {
		return repository.save(permissoes);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<PermissoesEventoPresencial> listarTodos(){
		return repository.listAll();
	}

	public PermissoesEventoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
