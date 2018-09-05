package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.AtividadeOnline;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.AtividadeOnlineRepository;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;

@Service
public class AtividadeOnlineService {

	@Autowired
	private AtividadeOnlineRepository repository;

	@Autowired
	private NotificacaoService notificacoes;

	public AtividadeOnline salvar(AtividadeOnline atividade){
		return repository.save(atividade);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<AtividadeOnline> listarTodos() {
		return repository.listAll();
	}

	public AtividadeOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public List<AtividadeOnline> buscarPorEvento(Long id) {
		return repository.buscarPorEvento(id);
	}

}
