package com.gilmarcarlos.developer.gcursos.service.eventos.online;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoDTO;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.model.eventos.online.EventoOnline;
import com.gilmarcarlos.developer.gcursos.repository.eventos.online.EventoOnlineRepository;
import com.gilmarcarlos.developer.gcursos.service.notificacoes.NotificacaoService;

@Service
public class EventoOnlineService {

	@Autowired
	private EventoOnlineRepository repository;
			
	@Autowired
	private NotificacaoService notificacoes;

	public EventoOnline salvar(EventoOnline eventoOnline){

		if (eventoOnline.getId() == null) {
			eventoOnline.ativarEvento();
			eventoOnline.desativarPublicacao();
		} else {
			EventoOnline evento = repository.buscarPor(eventoOnline.getId());
			validarStatusPublicacao(eventoOnline, evento);
		}

		return repository.save(eventoOnline);
	}

	public void removerPublicacaoSeEstiverFechado(EventoOnline evento) {
		EventoOnline temp = repository.buscarPor(evento.getId());
		temp.desativarPublicacao();
		repository.save(temp);
	}

	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<EventoOnline> listarTodos() {
		return repository.listAll();
	}

	public Page<EventoOnline> listarTodos(Pageable pageable) {
		return repository.listarTodos(pageable);
	}


	public List<EventoOnline> listarTodosPublicados() {
		return repository.findByPublicadoTrue();
	}

	public EventoOnline buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	

	public void cancelar(Long id) {
		
		EventoOnline evento = buscarPor(id);
		evento.cancelarEvento();
		repository.save(evento);
			
		
	}

	public void ativar(Long id) {
		EventoOnline evento = buscarPor(id);
		evento.ativarEvento();
		repository.save(evento);
	}

	public void cancelarPublicacao(Long id) {
		EventoOnline evento = buscarPor(id);
		evento.desativarPublicacao();
		repository.save(evento);
	}

	public void publicar(Long id) throws EventoCanceladoException {
		EventoOnline evento = buscarPor(id);

		if (evento.getPermissoes() == null) {
			throw new NullPointerException("O evento não tem permissões configuradas");
		}

		evento.ativarPublicacao();
		repository.save(evento);
	}

	private void validarStatusPublicacao(EventoOnline eventoOnline, EventoOnline evento) {
		if (evento.isAtivo()) {
			eventoOnline.ativarEvento();
			validarPublicacao(eventoOnline, evento);
		} else {
			eventoOnline.cancelarEvento();
			eventoOnline.desativarPublicacao();
		}
	}

	private void validarPublicacao(EventoOnline eventoOnline, EventoOnline evento) {
		if (evento.isPublicado()) {
			try {
				eventoOnline.ativarPublicacao();
			} catch (Exception e) {
				eventoOnline.desativarPublicacao();
			}
		} else {
			eventoOnline.desativarPublicacao();
		}
	}

	public List<EventoOnline> buscarPorUsuario(Long id) {
		return repository.buscarPorUsuario(id);
	}

	public Page<EventoOnline> buscarPor(Long id, Pageable pageable) {
		return repository.buscarPor(id, pageable);
	}

	public List<EventoDTO> listarTodosDTO() {
		List<EventoDTO> lista = new ArrayList<>();
		listarTodos().forEach( e -> lista.add(new EventoDTO(e)));
		return lista;
	}

}
