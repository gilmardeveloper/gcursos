package com.gilmarcarlos.developer.gcursos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.EventoPresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.DataFinalMenorException;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.EventoCanceladoException;
import com.gilmarcarlos.developer.gcursos.repository.EventoPresencialRepository;

@Service
public class EventoPresencialService {

	@Autowired
	private EventoPresencialRepository repository;

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
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}

	public List<EventoPresencial> listarTodos() {
		return repository.listAll();
	}

	public EventoPresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}

	public void cancelar(Long id) {
		EventoPresencial evento = buscarPor(id);
		evento.cancelarEvento();
		repository.save(evento);
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
		if(evento.isPublicado()) {
			try {
				eventoPresencial.ativarPublicacao();
			}catch (Exception e) {
				eventoPresencial.desativarPublicacao();
			}
		}else {
			eventoPresencial.desativarPublicacao();
		}
	}
}
