package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.UnidadeTrabalho;
import com.gilmarcarlos.developer.gcursos.repository.locais.UnidadeTrabalhoRepository;

@Service
public class UnidadeTrabalhoService {

	@Autowired
	private UnidadeTrabalhoRepository repository;
	
	
	public UnidadeTrabalho salvar(UnidadeTrabalho unidadeTrabalho) {
		return repository.save(unidadeTrabalho);
	}
	
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	

	public List<UnidadeTrabalho> listarTodos(){
		return repository.listAll();
	}
	
	public List<UnidadeTrabalho> listarTodos(Long departamento) {
		return repository.listAll(departamento);
	}
	
	public List<UnidadeTrabalho> listarTodosSemFones(){
		return repository.listaTodos();
	}

	public UnidadeTrabalho buscarPor(Long id) {
		return repository.buscarPor(id);
	}


	
	
	
}