package com.gilmarcarlos.developer.gcursos.service.eventos.presencial;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DataFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.eventos.presencial.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.notifications.Notificacao;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.EventoPresencialRepository;
import com.gilmarcarlos.developer.gcursos.repository.eventos.presencial.InscricaoPresencialRepository;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;
import com.gilmarcarlos.developer.gcursos.utils.IconeTypeUtils;
import com.gilmarcarlos.developer.gcursos.utils.StatusTypeUtils;

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
	
	public Page<EventoPresencial> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}
	
	public Page<EventoPresencial> buscarPor(LocalDate inicio, LocalDate termino, Pageable pageable) throws DataFinalMenorException {
		if(termino.isBefore(inicio)) {
			throw new DataFinalMenorException();
		}
		return repository.buscarPor(inicio, termino, pageable);
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
				notificacoes.salvar(new Notificacao(i.getUsuario(), "Inscrição cancelada", IconeTypeUtils.INFORMACAO, StatusTypeUtils.INFORMACAO, "sua inscrição foi cancelada, porque o evento foi cancelado ( " + i.getEventoPresencial().getTitulo() + " )"));
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

	public List<EventoDTO> listarTodosDTO() {
		List<EventoDTO> lista = new ArrayList<>();
		listarTodos().forEach( e -> lista.add(new EventoDTO(e)));
		return lista;
	}

	public boolean sexoExiste(String nome) {
		return listarTodos().stream().anyMatch( e -> e.getPermissoes().getSexos().contains(nome));
	}

	public Page<EventoPresencial> buscarPorUsuario(Long id, Pageable pageable) {
		return repository.buscarPorUsuario(id, pageable);
	}

}
