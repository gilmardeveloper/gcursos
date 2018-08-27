package com.gilmarcarlos.developer.gcursos.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gilmarcarlos.developer.gcursos.model.eventos.AtividadePresencial;
import com.gilmarcarlos.developer.gcursos.model.eventos.exceptions.HoraFinalMenorException;
import com.gilmarcarlos.developer.gcursos.repository.AtividadePresencialRepository;

@Service
public class AtividadePresencialService {

	@Autowired
	private AtividadePresencialRepository repository;
	
	public AtividadePresencial salvar(AtividadePresencial atividade) throws HoraFinalMenorException {
		
		LocalTime horaInicio = atividade.getTimeInicio();
		LocalTime horaFim = atividade.getTimeFim(); 
		LocalTime horaIncioEvento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getTimeAbertura();
		LocalTime horaFimEvento = atividade.getDiaEvento().getProgramacaoPresencial().getEventoPresencial().getTimeTermino(); 		
		
		if(horaFim.isBefore(horaInicio)) {
			throw new HoraFinalMenorException();
		}
		
		if(horaInicio.isBefore(horaIncioEvento) || horaFim.isAfter(horaFimEvento)) {
			throw new HoraFinalMenorException("a hora inicial ou final da atividade excede o horário do evento");
		}
		
		return repository.save(atividade);
	}
		
	public void deletar(Long id) {
		repository.deleteById(id);
	}
			
	public List<AtividadePresencial> listarTodos(){
		return repository.listAll();
	}

	public AtividadePresencial buscarPor(Long id) {
		return repository.buscarPor(id);
	}
	
	public List<AtividadePresencial> buscarPorEvento(Long id) {
		return repository.buscarPorEvento(id);
	}
	
	
}
