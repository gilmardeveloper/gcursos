package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DataFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.model.type.IconeType;
import com.gilmarcarlos.developer.gcursos.model.type.StatusType;
import com.gilmarcarlos.developer.gcursos.repository.EventoPresencialRepository;
import com.gilmarcarlos.developer.gcursos.repository.InscricaoPresencialRepository;

@Service
public class EventoPresencialService {

	@Autowired
	private EventoPresencialRepository repository;
	
	@Autowired
	private InscricaoPresencialRepository inscricoesRepository;
	
	@Autowired
	private NotificacaoService notificacoes;

	public EventoPresencial salvar(EventoPresencial eventoPresencial) throws DataFinalMenorException {

		if (eventoPresencial.getDataTermino().isBefore(eventoPresencial.getDataInicio())) {
			throw new DataFinalMenorException();
		}

		if (eventoPresencial.getId() == null) {
			eventoPresencial.ativarEvento();
			eventoPresencial.desativarPublicacao();
		} else {
			EventoPresencial evento = repository.buscarPor(eventoPresencial.getId());
			validarStatusPublicacao(eventoPresencial, evento);
		}

		return repository.save(eventoPresencial);
	}

	public void removerPublicacaoSeEstiverFechado(EventoPresencial evento) {
		EventoPresencial temp = repository.buscarPor(evento.getId());
		temp.desativarPublicacao();
		repository.save(temp);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<EventoPresencial> listarTodos() {

		removerPublicaoSeEstiverFechado();
		return repository.listAll();
	}

	public Page<EventoPresencial> listarTodos(Pageable pageable) {

		removerPublicaoSeEstiverFechado();
		return repository.listarTodos(pageable);
	}

	private void removerPublicaoSeEstiverFechado() {
		for (EventoPresencial evento : repository.listAll()) {
			if (evento.isFechado() && evento.isPublicado()) {
				evento.desativarPublicacao();
				repository.save(evento);
			}
		}
	}

	public List<EventoPresencial> listarTodosPublicados() {
		return repository.findByPublicadoTrue();
	}

	public EventoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public List<EventoPresencial> buscarPorUsuario(Long id) {
		return repository.buscarPorUsuario(id);
	}

	public void cancelar(Long id) {
		
		EventoPresencial evento = buscarPor(id);
		evento.cancelarEvento();
		repository.save(evento);
		
		if(!evento.getInscricoes().isEmpty()) {
			evento.getInscricoes().forEach( i -> {
				notificacoes.salvar(new Notificacao(i.getUsuario(), "Inscrição cancelada", IconeType.INFORMACAO, StatusType.INFORMACAO, "sua inscrição foi cancelada, porque o evento foi cancelado ( " + i.getEventoPresencial().getTitulo() + " )"));
				inscricoesRepository.deleteById(i.getId());
			});
		}
		
	}

	public void ativar(Long id) {
		EventoPresencial evento = buscarPor(id);
		evento.ativarEvento();
		repository.save(evento);
	}

	public void cancelarPublicacao(Long id) {
		EventoPresencial evento = buscarPor(id);
		evento.desativarPublicacao();
		repository.save(evento);
	}

	public void publicar(Long id) throws EventoCanceladoException {
		EventoPresencial evento = buscarPor(id);

		if (evento.getPermissoes() == null) {
			throw new NullPointerException("O evento não tem permissões configuradas");
		}

		evento.ativarPublicacao();
		repository.save(evento);
	}

	private void validarStatusPublicacao(EventoPresencial eventoPresencial, EventoPresencial evento) {
		if (evento.isAtivo()) {
			eventoPresencial.ativarEvento();
			validarPublicacao(eventoPresencial, evento);
		} else {
			eventoPresencial.cancelarEvento();
			eventoPresencial.desativarPublicacao();
		}
	}

	private void validarPublicacao(EventoPresencial eventoPresencial, EventoPresencial evento) {
		if (evento.isPublicado()) {
			try {
				eventoPresencial.ativarPublicacao();
			} catch (Exception e) {
				eventoPresencial.desativarPublicacao();
			}
		} else {
			eventoPresencial.desativarPublicacao();
		}
	}

}
