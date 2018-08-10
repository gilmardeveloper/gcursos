package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.dados.complementares.Departamento;
import com.gilmarcarlos.developer.gcursos.repository.DepartamentoRepository;

@Service
public class DepartamentoService {

	@Autowired
	private DepartamentoRepository repository;
	
	public Departamento salvar(Departamento departamento) {
		return repository.save(departamento);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<Departamento> listarTodos(){
		return repository.listAll();
	}
	
	public Departamento buscarPor(Long id) {
		return repository.buscaPorId(id);
	}
	
	
}
