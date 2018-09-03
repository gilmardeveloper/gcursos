package com.gilmarcarlos.developer.gcursos.service.locais;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.locais.TelefoneUnidade;
import com.gilmarcarlos.developer.gcursos.repository.locais.TelefoneUnidadeRepository;

@Service
public class TelefoneUnidadeService {

	@Autowired
	private TelefoneUnidadeRepository repository;
	
	public TelefoneUnidade salvar(TelefoneUnidade telefoneUnidade) {
		return repository.save(telefoneUnidade);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public void deletarByUnidade(Long id) {
		repository.deleteByUnidade(id);
	}
			
	public List<TelefoneUnidade> listarTodos(){
		return repository.listAll();
	}
	
	
}
