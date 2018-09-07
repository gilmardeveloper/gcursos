package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.online.AtividadeOnline;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.OrdenarHelper;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.exceptions.PosicaoExisteException;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.AtividadeOnlineRepository;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;

@Service
public class AtividadeOnlineService {

	@Autowired
	private AtividadeOnlineRepository repository;

	@Autowired
	private NotificacaoService notificacoes;

	public AtividadeOnline salvar(AtividadeOnline atividade) throws PosicaoExisteException{
		
		for(AtividadeOnline a : atividade.getModulo().getAtividades()) {
			if((a.getPosicao() == atividade.getPosicao()) && !a.equals(atividade)) {
				throw new PosicaoExisteException();
			}
		}
		
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

	public void ordenar(List<OrdenarHelper> lista) {
		Integer posicao = 1;
		for(OrdenarHelper helper : lista) {
			AtividadeOnline atividade = buscarPor(helper.getId());
			atividade.setPosicao(posicao);
			repository.save(atividade);
			posicao++;
		}
	}

}
