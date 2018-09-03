package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.EnderecoUnidade;
import com.gilmarcarlos.developer.gcursos.repository.locais.EnderecoUnidadeRepository;

@Service
public class EnderecoUnidadeService {

	@Autowired
	private EnderecoUnidadeRepository repository;
	
	public EnderecoUnidade salvar(EnderecoUnidade enderecoUnidade) {
		return repository.save(enderecoUnidade);
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public void deletarByUnidade(Long id) {
		repository.deleteByUnidade(id);
	}
	
	public List<EnderecoUnidade> listarTodos(){
		return repository.listAll();
	}
	
	
}
