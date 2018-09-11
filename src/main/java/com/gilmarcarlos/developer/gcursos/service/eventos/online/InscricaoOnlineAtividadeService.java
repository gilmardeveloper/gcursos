package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.InscricaoOnlineAtividade;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.InscricaoOnlineAtividadeRepository;

@Service
public class InscricaoOnlineAtividadeService {

	@Autowired
	private InscricaoOnlineAtividadeRepository repository;
	
	public InscricaoOnlineAtividade salvar(InscricaoOnlineAtividade inscricao){
		return repository.save(inscricao);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<InscricaoOnlineAtividade> listarTodos() {
		return repository.listAll();
	}

	public InscricaoOnlineAtividade buscarPor(Long id) {
		return repository.buscarPor(id);
	}

}
