package com.gilmarcarlos.developer.gcursos.service.eventos.categorias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.categorias.CategoriaEvento;
import com.gilmarcarlos.developer.gcursos.repository.eventos.categorias.CategoriaEventoRepository;

@Service
public class CategoriaEventoService {

	@Autowired
	private CategoriaEventoRepository repository;
	
	public CategoriaEvento salvar(CategoriaEvento categoria) {
		return repository.save(categoria);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<CategoriaEvento> listarTodos(){
		return repository.listAll();
	}

	public CategoriaEvento buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	
}
