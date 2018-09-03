package com.gilmarcarlos.developer.gcursos.service.usuarios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.usuarios.NomeColaborador;
import com.gilmarcarlos.developer.gcursos.repository.usuarios.NomeColaboradorRepository;

@Service
public class NomeColaboradorService {

	@Autowired
	private NomeColaboradorRepository repository;
	
	public NomeColaborador salvar(NomeColaborador nomeColaborador) {
		return repository.save(nomeColaborador);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	@Cacheable("postCache")
	public List<NomeColaborador> listarTodos(){
		return repository.listAll();
	}

	public NomeColaborador buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
