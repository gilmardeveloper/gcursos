package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.Modulo;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.ModuloRepository;

@Service
public class ModuloService {

	@Autowired
	private ModuloRepository repository;
	
	public Modulo salvar(Modulo modulo) {
		return repository.save(modulo);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Modulo> listarTodos(){
		return repository.listAll();
	}

	public Modulo buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
